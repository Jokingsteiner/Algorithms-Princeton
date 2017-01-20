public class Dog extends Animal {
    void eat() {
        System.out.println("Dog is eating."); 
    }
    
    void run() {
        System.out.println("Dog is running.");  
    }
    
    public static void main(String arg[]){
        Animal a = new Dog();
        a.eat();
        a.run();
    }
}