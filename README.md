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
먼저 자바에서는 보통 이렇게 한다.  

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
            // 반대로 한번 업캐스팅을 해야 한다고 합니다. (Cookie -> Object)
            // 다운캐스팅 정리 : https://okky.kr/article/1163996
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
            CoconutCookie cookie = (CoconutCookie) super.clone();
            return cookie;
        }
        catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
     }
     
     // 이렇게 하면 업캐스팅 했으니까 돌아갈 
     
     

### 장점
### 단점
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
