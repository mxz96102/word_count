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