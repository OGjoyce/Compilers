echo '########################################'
echo '####  con lexer de coolc           #####'
echo '########################################'
perl pa2-grading.pl
mv grading/myparser grading/myparser.bak
echo '#!/bin/csh -f' > grading/myparser
echo '../../PA1/lexer $* | ../parser $*' >> grading/myparser
chmod +x grading/myparser

echo '########################################'
echo '####  con su lexer                 #####'
echo '########################################'

if [ -d ../PA1/ ]; then
	cd grading
	./143publicgrading PA3
else
	echo 'no se encuentra la carpeta PA1'
fi

