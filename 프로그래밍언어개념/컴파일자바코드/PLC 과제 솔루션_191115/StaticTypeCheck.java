// StaticTypeCheck.java

import java.io.IOException;
import java.util.*;

// Static type checking for Clite is defined by the functions 
// V and the auxiliary functions typing and typeOf.  These
// functions use the classes in the Abstract Syntax of Clite.


public class StaticTypeCheck {
	 	private static Type returnType;
	    private static boolean returnFound = false;
	    private static TypeMap functionMap = new TypeMap();
	    private static Functions dtFunction = new Functions();

    public static TypeMap typing (Declarations d) {
        TypeMap map = new TypeMap();
        for (Declaration di : d)  //declations�� ���ҵ� �ݺ�
            map.put (di.v, di.t); //declations���� declarion��ü�� variable�� Type��ü�� map�� ����
        return map; //map����
    }

    public static void check(boolean test, String msg) { //test�Ű� ������ 1�̸� �ƹ��͵� ���ϰ� 0�̸� msg���� ���� �޼��� ���
        if (test)  return;	
        System.err.println(msg);
       //System.exit(1);
    }
    

    public static void V (Declarations d) {  //declaratoin�߿� variable�� �ߺ��Ȱ� �ִ��� üũ�ϴ� �Լ�
        for (int i=0; i<d.size() - 1; i++)
            for (int j=i+1; j<d.size(); j++) {
                Declaration di = d.get(i);
                Declaration dj = d.get(j);
                check( ! (di.v.equals(dj.v)),
                       "duplicate declaration: " + dj.v);
            }
    } 

    public static void V (Program p) { 
    	//10.1
    	 Declarations ds = new Declarations();
         ds.addAll(p.global);
         
         for (int i=0; i<p.functions.size(); i++) { //
             Variable fl = new Variable(p.functions.get(i).id);
             ds.add(new Declaration(fl,p.functions.get(i).t));
             functionMap.put(fl, p.functions.get(i).t);
         }
         V(ds);
         //10.1
         
         //10.2
         for (Function func : p.functions){
             V(func);
         }
         //10.2
         
       //10.3
         TypeMap tm = typing(ds);
         V(p.functions,tm);
       //10.3
         
    }
    
    public static void V (Function f){ //�Լ� �Ķ��Ÿ, �������� ������ ����ũ �ľ�(10.2)
        
        Declarations ds = new Declarations();
        ds.addAll(f.params);
        ds.addAll(f.local);
        V(ds); 
    }
    
    public static void V (Functions f, TypeMap tm) { //10.3
        for (Function func : f) {
            TypeMap fMap = new TypeMap();
            fMap.putAll(tm);
            fMap.putAll(typing(func.params));
            fMap.putAll(typing(func.local));
            
            V(func, fMap);
            fMap.putAll(functionMap);
            System.out.println("Function " + func.id + " = {");
            fMap.display(f,functionMap);
        }
    }
    
    public static void V(Function f, TypeMap tm){ //10.4
    	returnType = f.t;
    	returnFound = false;
        V(f.body,tm);

        // 10.4

        if (returnType.toString()!=Type.VOID.toString() && !f.id.equals("main")){
            check((returnFound == true),
                f.id + " is a non-Void function with no Return Statement");
        }
        //10.4
    }
    

    public static Type typeOf (Expression e, TypeMap tm, TypeMap fm) { //���� ���� type�� ���� �������ִ� �Լ�
        if (e instanceof Value) return ((Value)e).type; //���� �׳� ��value�� ���´ٸ� tm�� mapȮ�����ʿ� ���� �� ���� type����
        if (e instanceof Variable) { //���� �����̸�variable�� ���´ٸ� tm�� map���� �ش� �����̸��� �ִ��� Ȯ�����ְ� ������ type����
            Variable v = (Variable)e;
            check (tm.containsKey(v), "undefined variable: " + v);
            return (Type) tm.get(v);
        }
        
        if (e instanceof Call){
            Call c = (Call)e;
            if (functionMap.isEmpty()){
               functionMap = new TypeMap();
               functionMap.putAll(fm);
            }
            check (functionMap.containsKey(new Variable(c.name)), "undefined variable: " + c.name);
            return (Type) functionMap.get(new Variable(c.name));
        }
        
        if (e instanceof Binary) { //���� ���׿����ڸ� 
            Binary b = (Binary)e;
            if (b.op.ArithmeticOp( )) //���߿����� 
                if (typeOf(b.term1,tm,fm).toString()== Type.FLOAT.toString()) //���� ù��° ���갪�� float�� ��ü������ float�� ����
                    return (Type.FLOAT);
                else return (Type.INT); //int�� int�� ����
            if (b.op.RelationalOp( ) || b.op.BooleanOp( )) // ||�� && �̰ų� ���迬�����̸� boolŸ���� ����
                return (Type.BOOL);
        }
        
        if (e instanceof Unary) { //���� ���׿����ڸ�
            Unary u = (Unary)e;
            if (u.op.NotOp( ))        return (Type.BOOL); //!�����ڸ� boolŸ�� ����
            else if (u.op.NegateOp( )) return typeOf(u.term,tm,fm); //-�����ڸ� Ÿ���� �Ȱ����״� �׳� u�ȿ� �ִ� exprssion��ü Ÿ�� ����
            else if (u.op.intOp( ))    return (Type.INT); //����ȯint �� int��ȯ
            else if (u.op.floatOp( )) return (Type.FLOAT); //float > flaot
            else if (u.op.charOp( ))  return (Type.CHAR); //char > char
        }
        System.out.println(e.toString());
        throw new IllegalArgumentException("should never reach here");
    } 

    public static void V (Expression e, TypeMap tm) { //������ expression��ü�� type�� �ùٸ��� üũ���ִ� �Լ�
        if (e instanceof Value) //�׳� ��value�� �ƹ� ���� ���� �Լ� ����
            return;
        if (e instanceof Variable) {  //�����̸� variable�� �´ٸ�
            Variable v = (Variable)e;
            check( tm.containsKey(v) //tm�� map���� �ش� ������ ã�� ������ check�Լ� ����
                   , "undeclared variable: " + v);
            return;
        }
        
        if (e instanceof Call){
            Call c = (Call)e;
            check(functionMap.get(new Variable(c.name)).toString()!=Type.VOID.toString(),
                    "Expression Calls must have a return type.");
            for (Function func : dtFunction){
                if (func.id.equals(c.name)){
                    check (c.args.size() == func.params.size(),
                            "Arguments and Parameters are different size.");
                    for(int i = 0; i < c.args.size(); i++){
                        Type ti =((Type)func.params.get(i).t);
                        Type tj = typeOf(c.args.get(i),tm,functionMap); 
                        check(ti.equals(tj)
                                , func.params.get(i).t + " is not equal to " + typeOf(c.args.get(i),tm,functionMap));
                    }
                }
            }
            return;
        }
        
        if (e instanceof Binary) { //���� �������̸�
            Binary b = (Binary) e;
            Type typ1 = typeOf(b.term1, tm,functionMap); //��ü b�� term1�� Ÿ����  typ1�� ����
            Type typ2 = typeOf(b.term2, tm,functionMap); //��ü b�� term2�� Ÿ����  typ2�� ����
            V (b.term1, tm); //����Լ��� ���� term1,2Ȯ��
            V (b.term2, tm);
            if (b.op.ArithmeticOp( ))   
                check( typ1.toString() == typ2.toString() && //typ1�� 2�� Ÿ���� int�� float���� �ϳ��� �����ؾ��� > check�Լ� �̿��ؼ� Ȯ��
                       (typ1.toString() == Type.INT.toString() || typ1.toString() == Type.FLOAT.toString())
                       , "type error for " + b.op);
            else if (b.op.RelationalOp( )) //���� ���迬���ڸ� typ1�� 2�� �����ؾ��� > check�Լ� �̿�
                check( typ1.toString() == typ2.toString() , "type error for " + b.op);
            else if (b.op.BooleanOp( )) 
                check( typ1.toString() == Type.BOOL.toString() && typ2.toString() == Type.BOOL.toString(),
                       b.op + ": non-bool operand");
            else
                throw new IllegalArgumentException("should never reach here");
            return;
        }
        
        
        // student exercise
        if (e instanceof Unary) { //���� �����ڸ�
            Unary u = (Unary) e;
            Type t = typeOf(u.term, tm,functionMap); //��ü u�� termŸ���� typeof�Լ��� �̿��� t�� ����
            V (u.term, tm); //����Լ��� ���� termȮ��
            if (u.op.NotOp()) { //���� !��� t��  boolŸ������ check�Լ��� ���� Ȯ��
                check(t.toString()==Type.BOOL.toString(), "type error for " + u.op);
            }
            else if (u.op.NegateOp()) { //���� - ��� t�� float�̳� intŸ������ check�Լ��� ���� Ȯ��
                check(t.toString()==Type.FLOAT.toString() || t.toString()==Type.INT.toString(),
                      "type error for " + u.op);
            }
            else if (u.op.floatOp()) { //���� float����ȯ�̸� t�� intŸ������ check�Լ��� ���� Ȯ��
                check(t.toString()==Type.INT.toString(), "type error for " + u.op);
            }
            else if (u.op.charOp()) { //���� char����ȯ�̸� t�� intŸ������ check�Լ��� ���� Ȯ��
                check(t.toString()==Type.INT.toString(), "type error for " + u.op);
            }
            else if (u.op.intOp()) { //���� int����ȯ�̸� t�� char�Ǵ� floatŸ������ check�Լ��� ���� Ȯ��
                check(t.toString()==Type.FLOAT.toString() || t.toString()==Type.CHAR.toString(),
                      "type error for " + u.op);
            }
            else throw new IllegalArgumentException("reached unary else");
            return;
        }
        //
        
        throw new IllegalArgumentException("should never reach here");
    }

    public static void V (Statement s, TypeMap tm) { //statement�� �ùٸ��� Ȯ���ϴ� �Լ�
        if ( s == null )
            throw new IllegalArgumentException( "AST error: null statement");
        if (s instanceof Skip) return; //skip�̸� �ƹ��͵� �����ʰ� �Լ� ����
        if (s instanceof Assignment) { //���� �Ҵ����ϴ� �����̶�� 
            Assignment a = (Assignment)s;
            check( tm.containsKey(a.target) //���� tm�� map�� target�� �̸��� ���� ������ �ִ��� check�Լ��� ���� �˻�
                   , " undefined target in assignment: " + a.target);
            V(a.source, tm); //expression�� �ùٸ��� Ȯ���ϴ� ���� V�Լ��� �̿��� Ȯ��
            Type ttype = (Type)tm.get(a.target); //tm�� map���� target�� Ÿ���� ttype������ ����
            Type srctype = typeOf(a.source, tm, functionMap); //exprssion�� ��ü�� typeof�Լ��� �̿��� �� Ÿ���� srctype�� ����
            if (ttype.toString() != srctype.toString()) { //���� 2���� Ÿ���� �ٸ��ٸ�
                if (ttype.toString() == Type.FLOAT.toString()) //�ٵ� float�� int�� ��ȣȣȯ�� ���� > check�Լ��̿�
                    check( srctype.toString() == Type.INT.toString()
                           , "mixed mode assignment to " + a.target);
                else if (ttype.toString() == Type.INT.toString()) //�ٵ�  int�� char�� ��ȣȣ���� ����
                    check( srctype.toString() == Type.CHAR.toString()
                           , "mixed mode assignment to " + a.target);
                else //�� 2���� ��츻��� ������ �߻�
                    check( false
                           , "mixed mode assignment to " + a.target);
            }
            return;
        } 
 
        // student exercise
        if (s instanceof Conditional) { //���� if���̶��
            Conditional c = (Conditional) s;
            V(c.test, tm); //c�� test�� exprssion�� �˻��ϴ� V�Լ��� ����
            check(typeOf(c.test, tm, functionMap)==Type.BOOL, //�׸��� test�� Ÿ���� bool���� check�Լ��� �̿��� �˻�
                  "conditional test must return bool in " + c.test);
            V(c.thenbranch, tm); //����Լ��� �̿��� then�� else statement�˻�
            V(c.elsebranch, tm);
            return;
        }
        if (s instanceof Loop) { //���� �ݺ����̶��
            Loop l = (Loop) s;
            V(l.test, tm); //l�� test�� exprssion�� �˻��ϴ� V�Լ��� ����
            check(typeOf(l.test, tm, functionMap)==Type.BOOL, // �׸��� test�� Ÿ���� bool���� check�Լ��� �̿��� �˻�
                  "loop test must return bool in " + l.test);
            V(l.body, tm); //����Լ��� �̿��� body�κ� �˻�
            return;
        }
        if (s instanceof Block) {  //���� ����̶��
            Block b = (Block) s;
            if (b.members.size() == 0) return; //��Ͼȿ� statement��ü�� ������ �׳� ����
            for (int i = 0; i < b.members.size(); i++) { //statement��ü�� ����Լ��� �̿��� �˻�
                V(b.members.get(i), tm);
            }
            return;
        }
        //
        if (s instanceof Return)
        {
            //10.5
            check(!(returnType.toString()==Type.VOID.toString()),    
                "Return is not a valid Statement in a Void Function");
            Return r = (Return)s;
            //10.5
            //10.4
            check(returnType.toString()==typeOf(r.result,tm, functionMap).toString(),
                "The returned type does not match the fuction type;");
            returnFound = true;
            return;
            //10.4
        }
        
        if (s instanceof Callstatement) {
        	Callstatement c = (Callstatement)s;
            check((functionMap.get(new Variable(c.name))).toString()==Type.VOID.toString(),
                    "Statement Calls can only be to Void statements");
            for (Function func : dtFunction){
                if (func.id.equals(c.name)){
                    check (c.args.size() == func.params.size(),
                            "Arguments and Parameters are different size.");
                    for(int i = 0; i < c.args.size(); i++){
                        Type ti =((Type)func.params.get(i).t);
                        Type tj = typeOf(c.args.get(i),tm, functionMap); 
                        check(ti.equals(tj)
                                , func.params.get(i).t + " is not equal to " + typeOf(c.args.get(i),tm, functionMap));
                    }
                }
            }
            return;
        }
        
        throw new IllegalArgumentException("should never reach here");
    }

    public static void main(String args[]) throws IOException {
        Parser parser  = new Parser(new Lexer(args[0])); //lexer�� parser��� �����ϴ� ����
        Program prog = parser.program();
        prog.display(args[0]);           // student exercise
        System.out.println("\nBegin type checking...");
        System.out.println("Type map:");
        //TypeMap map = typing(prog.decpart);
        //System.out.println(map.display());   // student exercise
        V(prog);
        System.out.println("No type errors");
    } //main

} // class StaticTypeCheck

