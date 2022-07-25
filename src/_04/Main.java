package _04;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Cookie cookie = new Cookie(new Source("choco"));
        Cookie cookie1 = (Cookie) cookie.clone();

        System.out.println(cookie != cookie1);
        System.out.println(cookie.equals(cookie1));

        BlackCookie blackCookie = new BlackCookie(new Source("blackChoco"));
        BlackCookie blackCookie1 = (BlackCookie) blackCookie.clone();

        System.out.println(blackCookie != blackCookie1);
        System.out.println(blackCookie.equals(blackCookie1));

        BlueCookie blueCookie = new BlueCookie(new Source("blueChoco"));
        BlueCookie blueCookie1 = (BlueCookie) blueCookie.clone();

        System.out.println(blueCookie != blueCookie1);
        System.out.println(blueCookie.equals(blueCookie1));

    }
}
