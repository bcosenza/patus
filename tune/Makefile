JFLAGS = -d stencil_tune/bin -cp ./stencil_tune/src
JC = javac

.SUFFIXES: .java .class


CLASSES = $(shell ls stencil_tune/src/stenciltune/*.java)

default: .class .jar .txt

#classes: $(CLASSES:.java=.class)

.class:
	$(JC) $(JFLAGS) $(CLASSES)

.jar: 
	jar cvf jar/stenciltune.jar -C stencil_tune/bin .
.txt:
	java -cp jar/stenciltune.jar stenciltune.ExampleGenerator
clean:
	$(RM) -r stencil_tune/bin/* jar/stenciltune.jar

