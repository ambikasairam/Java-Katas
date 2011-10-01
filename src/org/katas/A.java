package org.katas;

import java.io.Serializable;

/**
 * A program to test whether Java is pass-by-value or pass-by-reference.
 * 
 * @author BJ Peter DeLaCruz
 */
public class A implements Serializable {

  /** Used for object serialization. */
  private static final long serialVersionUID = 1L;
  /** Some string. */
  private String string;
  /** Some other object. */
  private B b;

  /**
   * Constructor used to test whether Java is pass-by-value or pass-by-reference.
   * 
   * @param string Some string.
   * @param b Some other object used for testing.
   */
  public A(String string, B b) {
    this.string = string;
    this.b = b;
  }

  /**
   * Gets the test object.
   * 
   * @return The test object.
   */
  public B getB() {
    return this.b;
  }

  /**
   * Sets the string variable.
   * 
   * @param string Value used to set string.
   */
  public void setString(String string) {
    this.string = string;
  }

  /**
   * Gets the value of string.
   * 
   * @return The value of string.
   */
  public String getString() {
    return this.string;
  }

  /**
   * Makes b in this object point to null.
   */
  public void nullify() {
    this.b = null;
  }

  /**
   * The test program.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    B b = new B();
    A a = new A("Hello World", b);
    b = null;
    System.out.println(a.getB()); // returns a non-null object

    B b1 = new B();
    B b2 = b1;
    A a1 = new A("Hello World", b2);
    b1 = null;
    System.out.println(a1.getB()); // returns a non-null object

    System.out.println(b1);
    System.out.println(b2); // b2 is non-null (pointer cannot be changed, unlike C)

    B b3 = new B();
    A a2 = new A("Test", b3);
    a2.nullify();
    System.out.println(a2.getB());
    System.out.println(b3); // b3 is non-null (again, pointer cannot be changed)

    B b4 = new B("Bye Bye");
    A a3 = new A("", b4);
    a3.getB().setString("Goodbye");
    System.out.println(b4.getString()); // prints "Goodbye", not "Bye Bye"
    B b5 = b4;
    System.out.println(b5.getString()); // also prints "Goodbye", not "Bye Bye"
    b4.setString("Ciao");
    System.out.println(b4.getString());
    System.out.println(b5.getString()); // "Ciao" is printed twice

    // Lesson:
    // In Java, you pass a pointer to an object to a method, and anything you do to change the
    // values of the variables inside the object to which that pointer points is also reflected
    // outside the method. But in a method, you cannot change the pointer to point to something
    // something else. In other words:
    //     public void M1(B b) { this.b = b; this.b = null; }
    // If b is non-null when it is passed to M1, b is also non-null after the method returns.
    //     public void M2(B b) { this.b = b; this.b.setString("something"); }
    // string in b outside M2 is changed.
  }
}
