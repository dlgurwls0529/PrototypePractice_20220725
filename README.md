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

     <Cookie.class, Prototype>
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
     
     
     <CoconutCookie.class, ConcretePrototype>
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
     
     <CoconutCookie.class, ConcretePrototype>
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

### 장점
https://keichee.tistory.com/173  
https://sourcemaking.com/design_patterns/prototype  
https://songhayoung.github.io/2020/08/13/Design%20Pattern/PrototypePattern/#%ED%99%9C%EC%9A%A9%EC%84%B1  

- 서브클래스를 필요로 하지 않는다. 빌더 패턴, 팩토리 메소드 패턴의 경우  
해당 객체를 생성하기 위해서는 인터페이스와 그것을 구현하는 서브클래스가 필요하다.  
(Product 클래스 이외에 생성 과정을 위한 별도의 클래스)  
반면 프로토타입을 이용한 생성 방식은 clone 메소드를 구현하는 것만드로 생성 절차를  
수행할 수 있다.  
- 객체 생성 '절차'에서 필요한 비용이 엄청 클 때(HTTP 혹은 DB 다녀와서 필드를 주입)  
그냥 한 번 생성된 프로토타입 객체를 복사하여 쓰므로, 효율적이다.  
- Abstract Factory, Factory method, Builder, Singleton 등의 다른 생성 패턴과  
쉽게 응용되어 적용될 수 있다.  

### 단점
- 순환 참조가 있는 것을 고려해야 한다. 가령, 어떤 클래스의 필드에 다른 클래스의
- 깊은 복사와 얕은 복사에 대한 고민이 필요하다.
- 변하지 않는 객체를 못만든다. (setter가 필요함, Read-Only 객체라면 괜찮다.)
- 
### 깊은 복사와 얕은 복사
### 상속 관계에서의 적용
### 리플렉션
### 다운캐스팅
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
