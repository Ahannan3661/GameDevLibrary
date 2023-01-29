public class Exercise_05
{
    public static void main(String[] args)
    {
        Rectangle r = new Rectangle(0,0,5,5);
        System.out.println(r.left +" "+ r.top+" "+r.width);
        r.setSize(10,10);
        System.out.println(r.left +" "+ r.top+" "+r.width);
//        Vector A = new Vector(5,6);
//        Vector B = new Vector(7,8);
//        System.out.println(A);
//        System.out.println(B);
//        System.out.println(add(A,B));
    }
    public static Vector add(Vector A , Vector B)
    {
        Vector c = new Vector();
        c.setValues(A.x+B.x,A.y+B.y);
        return c;
    }
}
