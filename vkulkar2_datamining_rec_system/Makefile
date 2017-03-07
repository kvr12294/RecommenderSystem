SRCS = $(wildcard src/*.java)
CLS  = $(SRCS:.java=.class)

default:
	javac -classpath . $(SRCS)

run:
	java -cp ./src Rsdm train_all_txt.txt output.txt

clean:
	$(RM) $(CLS)