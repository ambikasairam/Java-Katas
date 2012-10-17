package org.katas;

import java.util.StringTokenizer;

/**
 * Java Kata: URL Rewrite.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class Url {

    /**
     * Given a URL, this method parses the URL according to the rules found <a
     * href="http://codingkata.org/katas/unit/url-rewrite"
     * target="_blank">here</a>.
     * 
     * @param url
     *            Original URL to parse.
     * @return Modified URL if any one of the rules is applied, original URL
     *         otherwise.
     */
    public static String rewrite(String url) {
        StringTokenizer st = new StringTokenizer(url, "/");
        if (st.countTokens() < 2 || st.countTokens() > 4) {
            return url;
        }

        String firstToken = st.nextToken();
        if (!"article".equals(firstToken) && !"guide".equals(firstToken)
                && !"company".equals(firstToken)) {
            return url;
        }

        StringBuffer buffer = new StringBuffer();
        char slash = '/';
        buffer.append(slash);
        buffer.append(firstToken);

        if (st.countTokens() == 1 && "article".equals(firstToken)) {
            buffer.append("?id=");
            buffer.append(st.nextToken());
            return buffer.toString();
        }

        if (st.countTokens() >= 2 && "guide".equals(firstToken)) {
            buffer.append(slash);
            buffer.append(st.nextToken());
            buffer.append(slash);
            int number = Integer.parseInt(st.nextToken()) + 1;
            buffer.append(number);
            if (st.hasMoreTokens()) {
                buffer.append(slash);
                buffer.append(st.nextToken());
            }
            return buffer.toString();
        }

        if (st.countTokens() == 2 && "company".equals(firstToken)) {
            buffer.append("?country=");
            buffer.append(st.nextToken());
            buffer.append("?city=");
            buffer.append(st.nextToken());
            return buffer.toString();
        }

        return url;

    }

    /**
     * Tests the rewrite method above.
     * 
     * @param args
     *            None.
     */
    public static void main(String[] args) {
        String result = Url.rewrite("/article/512/");
        System.out.println(result);
        result = Url.rewrite("/guide/srv/2008/index.html");
        System.out.println(result);
        result = Url.rewrite("/guide/os/2007/");
        System.out.println(result);
        result = Url.rewrite("/company/us/honolulu/");
        System.out.println(result);
        result = Url.rewrite("/guide/os/2008/win/xp");
        System.out.println(result);
        result = Url.rewrite("/state/50");
        System.out.println(result);
        result = Url.rewrite("/article/50/49");
        System.out.println(result);
    }
}
