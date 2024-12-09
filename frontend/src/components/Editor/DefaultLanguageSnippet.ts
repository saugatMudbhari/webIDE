const languageSnippets: { [key: string]: string } = {
    "javascript": `function helloWorld() {
    console.log("Hello, JavaScript!");
}`,
    "c": `#include <stdio.h>
          int main() {
          printf("Hello, C!");
          return 0;
}`,
    "c++": `#include <iostream>
            int main() {
            std::cout << "Hello, C++!" << std::endl;
            return 0;
}`,
    "java": `public class HelloWorld {
             public static void main(String[] args) {
                System.out.println("Hello, Java!");
    }
}`,
    "python": `def hello_world():
    print("Hello, Python!")

hello_world()`,
    "php": `<?php
function helloWorld() {
    echo "Hello, PHP!";
}
helloWorld();
?>`
};

export default languageSnippets;