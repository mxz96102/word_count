# 软件测试第二周作业 wordcount

## Github地址

https://github.com/mxz96102/word_count

## PSP2.1表格

| **PSP2.1**                              | **PSP** **阶段**                        | **预估耗时** **（分钟）** | **实际耗时** **（分钟）** |
| --------------------------------------- | --------------------------------------- | ------------------------- | ------------------------- |
| Planning                                | 计划                                    | 25                        | 30                        |
| · Estimate                              | · 估计这个任务需要多少时间              | 150                       | 252                       |
| Development                             | 开发                                    |                           |                           |
| · Analysis                              | · 需求分析 (包括学习新技术)             | 20                        | 20                        |
| · Design Spec                           | · 生成设计文档                          | 0                         | 0                         |
| · Design Review                         | · 设计复审 (和同事审核设计文档)         | 0                         | 0                         |
| · Coding Standard                       | · 代码规范 (为目前的开发制定合适的规范) | 2                         | 2                         |
| · Design                                | · 具体设计                              | 15                        | 30                        |
| · Coding                                | · 具体编码                              | 100                       | 150                       |
| · Code Review                           | · 代码复审                              | 10                        | 10                        |
| · Test                                  | · 测试（自我测试，修改代码，提交修改）  | 3                         | 60                        |
| Reporting                               | 报告                                    | 30                        | 30                        |
| · Test Report                           | · 测试报告                              | 5                         | 5                         |
| · Size Measurement                      | · 计算工作量                            | 5                         | 5                         |
| · Postmortem & Process Improvement Plan | · 事后总结, 并提出过程改进计划          | 20                        | 20                        |
|                                         | 合计                                    | 205                       | 312                       |

## 解题思路

分函数实现功能，并实现通过正则表达式来判断词，行，特殊行。

自制arg解析，方便命令行指令功能需求。

查阅资料：

https://stackoverflow.com/questions/25555717/intellij-idea-javafx-artifact-build-does-not-generate-exe

http://blog.csdn.net/soszou/article/details/7979060

## 程序设计实现过程

- 解析参数

  设计ArgsParser类

- 针对错误设计函数

  error

- 针对文件读写设计函数

  getFileContents，fileToWord，findfiles

- 针对复杂功能设计函数

  countWord，countLine，sumChar

- 整合功能以及输出

  countFile



## 代码说明

```
	/**
 	* 把文件内容按行读进ArrayList
 	* @param filepath {String}
 	* @return contents {ArrayList<String>}
 	*/

	private static ArrayList<String> getFileContents(String filepath)


	/**
     * 把文件中的词读进Array
     * @param filePath {String}
     * @return words {String[]}
     * @throws IOException
     */
    private static String[] fileToWord(String filePath)
    
    /**
     * 从文件建立禁用词set
     * @param banlistFile {String}
     * @return banwordset {Set<String>}
     */
    private static Set<String> buildBanWord(String banlistFile) 
    
    /**
     * 计算词数 (以 " " and ","分割)
     * @param str {String}
     * @param banWords {Set<String>}
     * @return count {int}
     */
    private static int countWord(String str, Set<String> banWords)


    /**
     * 将args实例化
     * @param args {String[]}
     */
        ArgsParser(String[] args) 
        
    /**
     * 计算各种特殊行的数量
     * @param contents {String[]}
     * @return [codeline, nullline, docline] {int[]}
     */
    private static int[] countline(String[] contents)
    
    /**
     * 从字符串数组中算总字符数
     * @param contents {String[]}
     * @return sum {int}
     */
    private static int sumChar(String[] contents)
    
    /**
     * 确认文件是否存在，不存在则建立
     * @param filepath {String}
     */
    private static void confirmFile(String filepath)
    
    /**
     * 总的count以及out的函数
     * @param filepath {String}
     * @param ap {ArgsParser}
     */
    private static void countFile(String filepath, ArgsParser ap, PrintWriter out)
    
    /**
     * 按名称寻找文件清单的函数
     * @param filepath {String}
     * @return filepathlist {String}
     */
    private static String[] findfiles(String filepath, boolean s)
```

## 测试设计过程

测试过程对于每一项功能单独测试，再进行组合测试，确保覆盖了所有可执行的代码，对文件名输入文件和文件夹也作出了测试，总共设计13个测试用例：

```bash
wc.exe
wc.exe -a
wc.exe -a ../test/test.c
wc.exe -l ../test/test.c -o out1.txt
wc.exe -w ../test/test.c -o out2.txt
wc.exe -a ../test/test.c -o out3.txt
wc.exe -c ../test/test.c -o out4.txt
wc.exe -l -w -a -c ../test/test.c -o out5.txt
wc.exe -a ../test/ -o out6.txt
wc.exe -a -s ../test/ -o out7.txt
wc.exe -a ../test/*.c -o out8.txt
wc.exe -a ../test/*.c -e ../stop.txt -o out9.txt
wc.exe  -l -w -a -c -s ../test/ -e ../stop.txt -o out10.txt
```

测试结果见项目下bin

```bash
out10.txt       out3.txt        out5.txt        out7.txt        out9.txt        
out1.txt        out2.txt        out4.txt        out6.txt        out8.txt        result.txt 
```

## 参考文献链接

http://www.cnblogs.com/ningjing-zhiyuan/p/8563562.html