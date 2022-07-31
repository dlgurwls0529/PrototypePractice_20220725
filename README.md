## 프로토타입 패턴(Prototype Pattern)
### 의미  
객체를 생성할 때, 그것의 프로토타입을 복사하고, 특정 부분만 수정하여    
객체를 생성하는 방법이다. 가령 어떤 회원 정보 클래스가  
객체를 생성하는 과정에서 DB를 한번 다녀온다고 해보자.  
이 경우 객체를 새로 생성하는 비용이 매우 크다. 그래서 다른 객체를  
생성할 때에는 이렇게 생성된 객체의 값을 복사한 clone을 만들고,  
일부 정보만 수정하여 이용하는 것이 프로토타입 패턴이다.

위키백과에 따르면 생성할 객체의 타입이 프로토타입 인스턴스로부터 결정(복제)  
되어 생성하는 것이다.  

### 구현 방법
언어마다 기능차이로 인해 살짝살짝 다른 느낌이 있다.  
먼저 자바에서는 보통 이렇게 한다. (얕은 복사) 

     <Cookie.class, ConcretePrototype>
     implements Cloneable
     // 자바에서 지원하는 clone 쓸려면 이 인터페이스 implements해야 한다.
     // clone메소드에서 호출자가 Cloneable implements안하면 에외 던지기 때문  
     // 근데 빈 인터페이스라서 뭔가 구현할 것은 없다.
     
     @Override
     public Object clone() {
        try {
            Cookie cookie = (Cookie) super.clone();
            // 오브젝트 타입으로 생성하면 오류뜸
            // 반환 타입이 Object라서 Client에서 쓸려면 Class로 다운캐스팅해야
            // 하는데, 어떤 객체를 다운캐스팅하려면 (Object -> Cookie). 
            // 반대로 한번 업캐스팅을 해야 한다고 한다. (Cookie -> Object)
            // 다운캐스팅 정리 : https://okky.kr/article/1163996
            // https://velog.io/@sezzzini/Java-Casting
            return cookie;
        }
        catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
     }
     
     
     <CoconutCookie.class, SubClassPrototype>
     extends Cookie
    
     <Main.class>
     Cookie cookie = new Cookie();
     Cookie cookie1 = (Cookie) cookie.clone();
     // 피호출자에서 리턴 시에 Object로 업캐스팅 한번 했으니까
     // Cookie로 다운캐스팅 해도 OK
     
     CoconutCookie coconutCookie = new CoconutCookie();
     CoconutCookie coconutCookie1 = coconutCookie.clone();
     // (Cookie -> Object), (Object -> CoconutCookie)
     // Cookie를 Object로 업캐스팅은 했지만, CoconutCookie를 Object
     // 로 업캐스팅은 안했기 때문에 타입캐스팅 에러가 뜬다.
     // 할려면 (CononutCookie -> Object)의 업캐스팅이 있어야 한다.
     // CoconutCookie 클래스에서도 clone을 오버라이딩하여 
     
     <CoconutCookie.class, SubClassPrototype>
     extends Cookie
     
     @Override
     public Object clone() {
        try {
            CoconutCookie coconutCookie = (CoconutCookie) super.clone();
            return coconutCookie;
        }
        catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
     }
     
     // 이렇게 하면 업캐스팅 했으니까 돌아갈 것이다. 
     // 하지만 이렇게 할 경우에는 얕은 복사로 인한 문제가 몇 가지 발생한다.
     // 깊은 복사를 위해서는 다음과 같이 코드를 추가한다.  
     // Cookie 클래스에서는 field1과 field2이, 
     // CoconutCookie 클래스에서는 field3이 참조형이라고 하면
     
     <Cookie.class>
     @Override
     public Object clone() {
        try {
            Cookie cookie = (Cookie) super.clone();
            cookie.setField1(...); 
            cookie.setField2(...);
            return cookie;
        }
        catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
     }
     
     <CoconutCookie.class>
     @Override
     public Object clone() {
        try {
            CoconutCookie coconutCookie = (CoconutCookie) super.clone();
            coconutCookie.setField1(...);
            coconutCookie.setField2(...);
            coconutCookie.setField3(...);
            return coconutCookie;
        }
        catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
     } 
     
정리하면 clone 메소드를 정의하는 인터페이스(Prototype)  
그러한 clone 메소드를 구현하는 ConcretePrototype  
그리고 그 프로토타입이 계층 구조를 가지고 있을 때,  
SubClassPrototype까지도 clone 메소드를 구현한다.  
이는 언어마다 너무 다르지만, 아무것도 없는 경우(Cloneable, Object.clone)  
할 수 있는 가장 기본적인 구현이다.  
이후 객체가 필요하면 기본 Prototype 객체를 클로닝하여 값을 바꿔 사용한다.  

https://jurogrammer.tistory.com/106

프로토타입 패턴은 프로토타입 레지스트리와 함께 구현되기도 한다.  
레지스트리는 프로토타입을 등록하고 꺼낼 수 있게 해주는 책임을 진다.  
다음은 레지스트리와 함께 구현한 프로토타입 패턴이다.  

     <Prototype.interface, Prototype>
     public interface Prototype {
          public Prototype clone();
     }
     
     <Shape.class, ConcretePrototype>
     public class Shape implements Prototype {

          private int x;
          private int y;

          public Shape(Shape prototype) {
               this.x =  prototype.x;
               this.y = prototype.y;
          }

          public Shape(int x, int y) {
               this.x = x;
               this.y = y;
          }

          @Override
          public Prototype clone() {
               Shape shape = new Shape(this);
               return shape;
          }

          public int getX() {
               return x;
          }

          public void setX(int x) {
               this.x = x;
          }

          public int getY() {
               return y;
          }

          public void setY(int y) {
               this.y = y;
          }
     }

     <Circle.class, SubClassPrototype>
     public class Circle extends Shape {

          private int r;

          public Circle(Circle prototype) {
               super(prototype);
               this.r = prototype.r;
          }

          public Circle(Shape prototype, int r) {
               super(prototype);
               this.r = r;
          }

          public Circle(int x, int y, int r) {
             super(x, y);
             this.r = r;
         }

         @Override
         public Prototype clone() {
             Circle circle = new Circle(this);
             return circle;
         }

         public int getR() {
             return r;
         }

         public void setR(int r) {
             this.r = r;
         }
     }

     <CircleRegistry.class, ConcretePrototypeRegistry>
     public class CircleRegistry {

          private static Map<String, Circle> map = new HashMap<>();

          public static void addItem(String id, Circle circle) {
               map.put(id, circle);
          }

          public static Circle getById(String id) {
               return (Circle) map.get(id).clone();
          }
     }

Registry 내부에 Map을 하나 필드로 두고, 거기에다가 Prototype 객체를  
메소드로써 등록하고 가져온다. 

     CircleRegistry.addItem("firstCircle", circle);
     Circle circle2 = CircleRegistry.getById("firstCircle");
     
잘 모르겠지만, 프로토타입을 좀 더 편리하게? 쓰기 위한 방법인 듯 하다.  



### 장점
https://keichee.tistory.com/173  
https://sourcemaking.com/design_patterns/prototype  
https://songhayoung.github.io/2020/08/13/Design%20Pattern/PrototypePattern/#%ED%99%9C%EC%9A%A9%EC%84%B1  
https://jurogrammer.tistory.com/106  
https://seokrae.gitbook.io/sr/java-1/design/creational/_prototype  
https://yardbirds.tistory.com/21  

- 서브클래스를 필요로 하지 않는다. 빌더 패턴, 팩토리 메소드 패턴의 경우  
해당 객체를 생성하기 위해서는 인터페이스와 그것을 구현하는 서브클래스가 필요하다.  
(Product 클래스 이외에 생성 과정을 위한 별도의 클래스)  
반면 프로토타입을 이용한 생성 방식은 clone 메소드를 구현하는 것만으로 생성 절차를  
수행할 수 있다.  
- 객체 생성 '절차'에서 필요한 비용이 엄청 클 때(HTTP 혹은 DB 다녀와서 필드를 주입)  
그냥 한 번 생성된 프로토타입 객체를 복사하여 쓰므로, 효율적이다.  
- Abstract Factory, Factory method, Builder, Singleton 등의 다른 생성 패턴과  
쉽게 응용되어 적용될 수 있다.  

### 단점
- 연쇄적인 참조를 고려해야 한다. 가령, 어떤 클래스의 필드에 다른 참조형 변수가 있고,  
그 변수 내에도 다시 참조형 변수가 있는 형태를 말한다. 이 경우에 얕은 복사를 통해 객체를  
복제한다면, 참조형 필드를 서로 공유하게 되므로, 이를 원하지 않는다면, 깊은 복사를 일일이  
구현해야 한다. 만약 그 참조가 순환 참조라면 더 골치아파진다.  
- 깊은 복사와 얕은 복사에 대한 고민이 필요하다.  
- 변하지 않는 객체를 못만든다. (setter가 필요함, 무의미하지만 Read-Only 객체라면 괜찮다.)  
- 프로토타입에 상속 계층이 있는 경우에 서브클래스마다 clone 메소드를 구현해야 할 수도 있다.  

### 깊은 복사와 얕은 복사
프로토타입 패턴에서 말하는 얕은 복사는 객체의 클로닝에서 필드를 얕은 복사하는 것이다.  
가령 어떤 클래스 A에 필드로 클래스 B의 객체가 있다면, 클로닝을 할 때 B의 필드가 아니라  
B의 주소를 복사하는 것이다.  

     A a = new A(new B());
     // a.b = 200(주소)
     A a_clone = (A) a.clone();
     // a_clone.b = 200(주소)
     
이렇게 되면 복제된 객체와 프로토타입 객체가 필드를 공유하게 되어, 하나가 변경되면 나머지    
도 역시 변경된다. 이러한 현상을 막기 위해서는 깊은 복사를 통해 객체를 클로닝해야 한다.  
(Java의 Object.clone 메소드는 얕은 복사 방식이다.)  

깊은 복사는 객체의 클로닝 시에 해당 객체의 모든 필드를 값 형식 단위로 복사하는 것이다.  
가령 위의 예시를 따르면  

     A a = new A(new B());
     // a.b = 200
     A a_clone = (A) a.clone();
     // a_clone.b = 300
     
깊은 복사 방식에서는 복제된 객체와 프로토타입 객체에서 필드 b가 다른 주소를 가지게 된다.  
(b를 공유하지 않게 된다.)  
이러한 깊은 복사는 참조 형식이 특히 복합 데이터 형식인 경우에 그 안에도 참조형이 있을 수  
있어서 구현이 굉장히 복잡해질 수 있다. 

### 리플렉션
리플렉션이란 클래스의 타입, 변수, 메소드에 동적으로 접근할 수 있게 해주는 API이다.  
https://sorjfkrh5078.tistory.com/298(API 라이브러리 프레임워크)  
무슨 말이냐면, 런타임에 클래스의 필드나 메소드 등에 접근한다는 것인데,  
정적으로 접근한다면  

     A a = new A();
     a.method();
     
method에 초점을 맞추면, method의 호출이 컴파일 타임에 결정이 된다.  
반면  

     String methodToCall = ~~(사용자로부터 받아오는 것)  
     A a = new A();
     a.call(methodToCall)
     
는 다르게 컴파일 타임에 a의 어떤 메소드를 호출할 지 정해지지 않았다.  
사용자가 런타임에 methodToCall을 "method"로 설정했다면, method가 실행될 것이고  
다른 경우라면, 다른 메소드가 실행되거나 예외가 터질 것이다.  
이처럼 Reflection API로써 동적으로 클래스의 요소에 접근할 수 있게 된다.  

말했듯이, 이러한 리플렉션이 필요한 상황은 역시 동적으로 클래스 요소에 접근할 때이다.    
클래스에 접근해서 필드 : 값 형식으로 출력하는 함수가 있고, 인자로 객체가 주어지며,  
그 객체가 런타임에 결정된다고 해보자.  
런타임에 결정되므로 다음과 같은 코드로는 불가능하다.

     public void printAllField(Object o) {
          System.out.println("age : ")
          // ????
     }

어떤 객체가 올지 모르기에, Object 타입 매개변수가 필요하고, 애초에 그 타입으로는  
인자로 올 필드가 뭔지 모르니까 뭘 출력할 지 알 수가 없다.  
(노가다로 모든 경우를 instanceof로 분기할 수 있지만, 그건 논외로 하겠다.)  
o 가 무슨 타입인지 몰라도 모든 필드에 접근 가능해야 한다.  

     public void printAllField(Object o) {
          Class<?> clazz = o.getClass();
          // class가 예약어라서 그나마 비슷한 clazz로 하는 것이다.  
          // o로부터 그 객체가 어떤 클래스로 생성된 것인지 가져온다.  
          // o가 Person 클래스 객체였다면 clazz 역시 Person이 된다.  
          // 일단 여기까지가 동적으로 클래스에 접근한 것이다. 
          Field[] fields = clazz.getDeclaredFields();
          // fields는 clazz의 필드를 담았다. 
          // Person 클래스라면 {Age, Name, Weight, ... } (실제랑 다름)  
          for(int i = 0; i < fields.length; i++) {
               fields[i].setAccessible(true);
               // private 필드일 수도 있으니까 접근 권한 해제한다.  
               // 밑에서 각 필드에 접근하게 된다.
               try {
                System.out.println(fields[i].getName() (필드의 이름)
                        + " : "
                        + fields[i].get(o) (필드의 값));
               } catch (IllegalAccessException e) {
                    e.printStackTrace();
               }
          }
     }

다음은 리플렉션으로 Deep Cloneing을 구현한 것이다.  
https://www.codeproject.com/Articles/1130717/Implementing-Deep-Cloning-using-Reflection  

### Java Casting
https://velog.io/@sezzzini/Java-Casting
캐스팅이란 타입을 변환하는 것이다.  

업 캐스팅이란
     
     Person person = new Student();
     // 혹은
     Student student = new Student();
     Person person = student;

위처럼 서브클래스 객체가 슈퍼클래스 타입으로 변환되는 것이다.(암시적 수행)  
저렇게 되면, person 객체에는 student 객체가 들어갔지만, Person 클래스의  
필드와 메소드밖에 접근하지 못한다. (오버라이딩 된 메소드는 접근 가능)

쓰는 이유는 다형성 때문이다. 

예를 들어서 void run(Person person) 이라는 메소드가 있다고 하자  
그리고 인자로 Student 타입 객체가 올지, Officer이나 Police 타입이 올지 모른다.  
그래서 Person 객체를 인자로 놓고 run 메소드를 추상 메소드를 기반으로 구현하게 되면  
구현 수정 없이 run(new Police()), run(new Officer()), run(new Student()) 전부 잘 돌아간다.  
(if else 없이도 어떤 타입이 오든 대응할 수 있게 된다.(Person을 상속 혹은 구현하는 타입))  

물론 오버라이딩 될 추상메소드 기반으로 구현된 경우에 한하기에, run 메소드에서 Student 같은  
Concrete Class의 고유한 필드나 구체적인 메소드에 접근할 수는 없다.  
그래서 다형성을 응용하기 위한 것이 아니라면 굳이 업캐스팅을 할 필요는 없다.(아닐 수도 있고)  

다운 캐스팅이란 

     Person person = new Student();
     // 위에서 업캐스팅 되어 만들어진 person 객체를
     Student student = (Student) person;
     // 이것처럼 Student 타입으로 "복구" 하는 것이다.  

이렇게 "업캐스팅이 선행되었을 때", 명시적 캐스팅으로, 타입을 복구하는 것이다.  
선행된 후가 아니라면 런타임에 ClassCastException이 발생하게 된다.

이 줄에서 student로는 Student의 모든 필드와 메소드에 접근 가능하나, person으로는 Person
필드와 메소드에만 접근이 가능하다. 상속관계에서 타입의 변화는 접근 가능한 필드와 메소드의  
변화라고 할 수도 있다.  
다운캐스팅은 Person 까지 접근 가능했던 것을 Student 범위까지 접근할 수 있게 열어주는 것이다.  
근데 만약 다운캐스팅을 수행하는 person 객체 안에 무엇이 들어있는지 모르므로 Person 타입이  
있다고 하자.  

     Person person = new Person();
     Student student = (Student) person;

그러면 Student로 다운캐스팅 했을 때, person의 범위를 Student 까지 열어주게 된다는 것인데,  
애초에 Person 안에는 Person 만큼의 내용밖에 없었기에 Student 범위까지 열 수 없다.  
뭔 소리냐면 Person 클래스에는 없던 필드인 grade라는 필드 자체가 내용에 없어서,
접근 권한이 grade 까지 열리면 말이 안된다는 것이다.  
만약 Student를 Person으로 업캐스팅 한 상황이라면, person 객체 안에는 Student 내용이 들어있으며,   
person을 통해서는 Person 타입 까지만 접근이 가능하다는 뜻이다. 일단 Student 내용이 있다는 것이  
자명하므로 person을 Student 타입으로써 접근해도 문제가 없다. 즉, 다운캐스팅이 잘 수행된다.  
성공적으로 다운캐스팅이 수행되면 오버라이딩된 메소드 뿐만 아니라 고유의 필드와 메소드에도 접근  
이 다시 가능하게 된다.  
https://okky.kr/article/1163996  
     
### 그 외
생성 패턴에 있는 프로토타입 패턴, 추상 팩토리 패턴, 빌더 패턴, 싱글턴 패턴  
각각은 같이 이용될 수도 있다. 가령 추상 팩토리 패턴에서 팩토리 메소드로 객체를  
생성할 때, 프로토타입을 가지고 있다가 그것을 clone하여 객체를 반환하는 것이다.  
이는 팩토리 메소드 패턴 역시 마찬가지이다. 

주로 생성 작업에 있어서 설계자는 팩토리 메소드 패턴으로 시작한다. (확장성)
그 다음에 추상 팩토리 패턴(묶음 생성? 이 필요할 때)
혹은 빌더 패턴(인자가 많거나 생성 절차가 복잡한 경우)
혹은 프로토타입 패턴(생성 비용이 크거나, 겹치는 필드가 많을 때)
으로 바뀔 수 있다.  
