#include <linux/dma-mapping.h>
#include <linux/dma-direct.h>
#include <linux/bootmem.h>
#include <linux/genalloc.h>
#include <asm/dma-mapping.h>
#include <linux/module.h>
#include <asm/page.h>

#define HEXAGON_MAPPING_ERROR	0

const struct dma_map_ops *dma_ops;
EXPORT_SYMBOL(dma_ops);

static inline void *dma_addr_to_virt(dma_addr_t dma_addr)
{
	return phys_to_virt((unsigned long) dma_addr);
}

static struct gen_pool *coherent_pool;


// Allocates from a pool of uncached memory that was reserved at boot time 

static void *hexagon_dma_alloc_coherent(struct device *dev, size_t size,
				 dma_addr_t *dma_addr, gfp_t flag,
				 unsigned long attrs)
{
	void *ret;


	 

	if (coherent_pool == NULL) {
		coherent_pool = gen_pool_create(PAGE_SHIFT, -1);

		if (coherent_pool == NULL)
			panic("Can't create %s() memory pool!", __func__);
		else
			gen_pool_add(coherent_pool,
				pfn_to_virt(max_low_pfn),
				hexagon_coherent_pool_size, -1);
	}

	ret = (void *) gen_pool_alloc(coherent_pool, size);

	if (ret) {
		memset(ret, 0, size);
		*dma_addr = (dma_addr_t) virt_to_phys(ret);
	} else
		*dma_addr = ~0;

	return ret;
}

static void hexagon_free_coherent(struct device *dev, size_t size, void *vaddr,
				  dma_addr_t dma_addr, unsigned long attrs)
{
	gen_pool_free(coherent_pool, (unsigned long) vaddr, size);
}

static int check_addr(const char *name, struct device *hwdev,
		      dma_addr_t bus, size_t size)
{
	if (hwdev && hwdev->dma_mask && !dma_capable(hwdev, bus, size)) {
		if (*hwdev->dma_mask >= DMA_BIT_MASK(32))
			printk(KERN_ERR
				"%s: overflow %Lx+%zu of device mask %Lx\n",
				name, (long long)bus, size,
				(long long)*hwdev->dma_mask);
		return 0;
	}
	return 1;
}

static int hexagon_map_sg(struct device *hwdev, struct scatterlist *sg,
			  int nents, enum dma_data_direction dir,
			  unsigned long attrs)
{
	struct scatterlist *s;
	int i;

	WARN_ON(nents == 0 || sg[0].length == 0);

	for_each_sg(sg, s, nents, i) {
		s->dma_address = sg_phys(s);
		if (!check_addr("map_sg", hwdev, s->dma_address, s->length))
			return 0;

		s->dma_length = s->length;

		if (attrs & DMA_ATTR_SKIP_CPU_SYNC)
			continue;

		flush_dcache_range(dma_addr_to_virt(s->dma_address),
				   dma_addr_to_virt(s->dma_address + s->length));
	}

	return nents;
}

// address is virtual
 
static inline void dma_sync(void *addr, size_t size,
			    enum dma_data_direction dir)
{
	switch (dir) {
	case DMA_TO_DEVICE:
		hexagon_clean_dcache_range((unsigned long) addr,
		(unsigned long) addr + size);
		break;
	case DMA_FROM_DEVICE:
		hexagon_inv_dcache_range((unsigned long) addr,
		(unsigned long) addr + size);
		break;
	case DMA_BIDIRECTIONAL:
		flush_dcache_range((unsigned long) addr,
		(unsigned long) addr + size);
		break;
	default:
		BUG();
	}
}
 
static dma_addr_t hexagon_map_page(struct device *dev, struct page *page,
				   unsigned long offset, size_t size,
				   enum dma_data_direction dir,
				   unsigned long attrs)
{
	dma_addr_t bus = page_to_phys(page) + offset;
	WARN_ON(size == 0);

	if (!check_addr("map_single", dev, bus, size))
		return HEXAGON_MAPPING_ERROR;

	if (!(attrs & DMA_ATTR_SKIP_CPU_SYNC))
		dma_sync(dma_addr_to_virt(bus), size, dir);

	return bus;
}

static void hexagon_sync_single_for_cpu(struct device *dev,
					dma_addr_t dma_handle, size_t size,
					enum dma_data_direction dir)
{
	dma_sync(dma_addr_to_virt(dma_handle), size, dir);
}

static void hexagon_sync_single_for_device(struct device *dev,
					dma_addr_t dma_handle, size_t size,
					enum dma_data_direction dir)
{
	dma_sync(dma_addr_to_virt(dma_handle), size, dir);
}

static int hexagon_mapping_error(struct device *dev, dma_addr_t dma_addr)
{
	return dma_addr == HEXAGON_MAPPING_ERROR;
}

const struct dma_map_ops hexagon_dma_ops = {
	.alloc		= hexagon_dma_alloc_coherent,
	.free		= hexagon_free_coherent,
	.map_sg		= hexagon_map_sg,
	.map_page	= hexagon_map_page,
	.sync_single_for_cpu = hexagon_sync_single_for_cpu,
	.sync_single_for_device = hexagon_sync_single_for_device,
	.mapping_error	= hexagon_mapping_error,
	.is_phys	= 1,
};

void __init hexagon_dma_init(void)
{
	if (dma_ops)
		return;

	dma_ops = &hexagon_dma_ops;
}