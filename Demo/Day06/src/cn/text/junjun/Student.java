package cn.text.junjun;

public class Student {

    public static void main(String[] args) {

        Student stu = new Student();

        stu.printMethod();
    }

    public  static  void  printMethod(){

        for(int j = 0; j < 2;j++){
            for(int i = 0; i < 2; i ++){
                System.out.print("*");
            }
            System.out.println();

        }

        int a,b,c;

        for( a = 1; a<=4;a++){

            for(c = 1; c <= 4-a; c++){
                System.out.print(" ");
            }

            for(b = 1; b <= 2*a-1; b++){
                System.out.print("*");
            }
            System.out.println();
        }


        for( a = 1; a <= 3; a++){

            for(c = 1; c <= a; c++){

                System.out.print(" ");
            }

            for(b = 1; b <= 7-2*a; b++){

                System.out.print("*");

            }
            System.out.println();


        }
    }




}

