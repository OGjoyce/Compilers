echo '#############################################'
echo '#### con lexer, parser, semant de coolc #####'
echo '#############################################'
perl pa4-grading.pl
mv grading/mycoolc grading/mycoolc.bak
echo '#!/bin/csh -f' > grading/mycoolc
echo 'if (-e ../Cgen.java) then' >> grading/mycoolc
echo '  ../../PA1/lexer $* | ../../PA2/parser $* | java -classpath /usr/class/cs143/cool/lib:../../PA3/:/usr/java/lib/rt.jar Semant $* | java -classpath /usr/class/cs143/cool/lib:..:/usr/java/lib/rt.jar Cgen $*' >> grading/mycoolc
echo 'else' >> grading/mycoolc
echo '  ../../PA1/lexer $* | ../../PA2/parser $* | ../../PA3/semant $* | ../cgen $*' >> grading/mycoolc
echo 'endif' >> grading/mycoolc
chmod +x grading/mycoolc

echo '########################################'
echo '#### con su lexer, parser y semant #####'
echo '########################################'

if [ -d ../PA1/ ]; then
	if [ -d ../PA2/ ]; then
		if [ -d ../PA3/ ]; then
			cd grading
			./143publicgrading PA5
		else
			echo 'no se encuentra la carpeta PA3'
		fi
	else
		echo 'no se encuentra la carpeta PA2'
	fi
else
	echo 'no se encuentra la carpeta PA1'
fi

