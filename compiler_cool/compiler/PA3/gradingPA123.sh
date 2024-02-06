echo '########################################'
echo '####  con lexer, parser de coolc   #####'
echo '########################################'
perl pa3-grading.pl
mv grading/mysemant grading/mysemant.bak
echo '#!/bin/csh -f' > grading/mysemant
echo 'if (-e ../Semant.java) then' >> grading/mysemant
echo '  ../../PA1/lexer $* | ../../PA2/parser $* | java -classpath /usr/class/cs143/cool/lib:..:/usr/java/lib/rt.jar Semant $*' >> grading/mysemant
echo 'else' >> grading/mysemant
echo '../../PA1/lexer $* | ../../PA2/parser $* | ../semant $*' >> grading/mysemant
echo 'endif' >> grading/mysemant
chmod +x grading/mysemant

echo '########################################'
echo '####  con su lexer  y parser       #####'
echo '########################################'

if [ -d ../PA1/ ]; then
	if [ -d ../PA2/ ]; then
		cd grading
		./143publicgrading PA4
	else
		echo 'no se encuentra la carpeta PA2'
	fi
else
	echo 'no se encuentra la carpeta PA1'
fi

