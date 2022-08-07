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
        for (Declaration di : d)  //declations의 원소들 반복
            map.put (di.v, di.t); //declations안의 declarion객체의 variable과 Type객체를 map에 전달
        return map; //map리턴
    }

    public static void check(boolean test, String msg) { //test매개 변수가 1이면 아무것도 안하고 0이면 msg관련 에러 메세지 출력
        if (test)  return;	
        System.err.println(msg);
       //System.exit(1);
    }
    

    public static void V (Declarations d) {  //declaratoin중에 variable이 중복된게 있는지 체크하는 함수
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
    
    public static void V (Function f){ //함수 파라메타, 지역변수 저장후 유니크 파악(10.2)
        
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
    

    public static Type typeOf (Expression e, TypeMap tm, TypeMap fm) { //값에 대한 type이 뭔지 리턴해주는 함수
        if (e instanceof Value) return ((Value)e).type; //만약 그냥 값value가 들어온다면 tm의 map확인할필요 없이 그 값의 type리턴
        if (e instanceof Variable) { //만약 변수이름variable이 들어온다며 tm의 map에서 해당 변수이름이 있는지 확인해주고 있으면 type리턴
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
        
        if (e instanceof Binary) { //만약 이항연산자면 
            Binary b = (Binary)e;
            if (b.op.ArithmeticOp( )) //그중에서도 
                if (typeOf(b.term1,tm,fm).toString()== Type.FLOAT.toString()) //만약 첫번째 연산값이 float면 전체적으로 float를 리턴
                    return (Type.FLOAT);
                else return (Type.INT); //int면 int를 리턴
            if (b.op.RelationalOp( ) || b.op.BooleanOp( )) // ||와 && 이거나 관계연산자이면 bool타입을 리턴
                return (Type.BOOL);
        }
        
        if (e instanceof Unary) { //만약 단항연산자면
            Unary u = (Unary)e;
            if (u.op.NotOp( ))        return (Type.BOOL); //!연산자면 bool타입 리턴
            else if (u.op.NegateOp( )) return typeOf(u.term,tm,fm); //-연산자면 타입은 똑같을테니 그냥 u안에 있는 exprssion객체 타입 리턴
            else if (u.op.intOp( ))    return (Type.INT); //형변환int 면 int반환
            else if (u.op.floatOp( )) return (Type.FLOAT); //float > flaot
            else if (u.op.charOp( ))  return (Type.CHAR); //char > char
        }
        System.out.println(e.toString());
        throw new IllegalArgumentException("should never reach here");
    } 

    public static void V (Expression e, TypeMap tm) { //실제로 expression객체가 type이 올바른지 체크해주는 함수
        if (e instanceof Value) //그냥 값value면 아무 문제 없이 함수 종료
            return;
        if (e instanceof Variable) {  //변수이름 variable이 온다면
            Variable v = (Variable)e;
            check( tm.containsKey(v) //tm의 map에서 해당 변수를 찾고 없으면 check함수 실행
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
        
        if (e instanceof Binary) { //이항 연산자이면
            Binary b = (Binary) e;
            Type typ1 = typeOf(b.term1, tm,functionMap); //객체 b의 term1의 타입을  typ1에 저장
            Type typ2 = typeOf(b.term2, tm,functionMap); //객체 b의 term2의 타입을  typ2에 저장
            V (b.term1, tm); //재귀함수를 통해 term1,2확인
            V (b.term2, tm);
            if (b.op.ArithmeticOp( ))   
                check( typ1.toString() == typ2.toString() && //typ1과 2의 타입이 int와 float둘중 하나로 동일해야함 > check함수 이용해서 확인
                       (typ1.toString() == Type.INT.toString() || typ1.toString() == Type.FLOAT.toString())
                       , "type error for " + b.op);
            else if (b.op.RelationalOp( )) //만약 관계연산자면 typ1와 2가 동일해야함 > check함수 이용
                check( typ1.toString() == typ2.toString() , "type error for " + b.op);
            else if (b.op.BooleanOp( )) 
                check( typ1.toString() == Type.BOOL.toString() && typ2.toString() == Type.BOOL.toString(),
                       b.op + ": non-bool operand");
            else
                throw new IllegalArgumentException("should never reach here");
            return;
        }
        
        
        // student exercise
        if (e instanceof Unary) { //단항 연산자면
            Unary u = (Unary) e;
            Type t = typeOf(u.term, tm,functionMap); //객체 u의 term타입을 typeof함수를 이용해 t에 저장
            V (u.term, tm); //재귀함수를 통해 term확인
            if (u.op.NotOp()) { //만약 !라면 t가  bool타입인지 check함수를 통해 확인
                check(t.toString()==Type.BOOL.toString(), "type error for " + u.op);
            }
            else if (u.op.NegateOp()) { //만약 - 라면 t가 float이나 int타입인지 check함수를 통해 확인
                check(t.toString()==Type.FLOAT.toString() || t.toString()==Type.INT.toString(),
                      "type error for " + u.op);
            }
            else if (u.op.floatOp()) { //만약 float형변환이면 t가 int타입인지 check함수를 통해 확인
                check(t.toString()==Type.INT.toString(), "type error for " + u.op);
            }
            else if (u.op.charOp()) { //만약 char형변환이면 t가 int타입인지 check함수를 통해 확인
                check(t.toString()==Type.INT.toString(), "type error for " + u.op);
            }
            else if (u.op.intOp()) { //만약 int형변환이면 t가 char또는 float타입인지 check함수를 통해 확인
                check(t.toString()==Type.FLOAT.toString() || t.toString()==Type.CHAR.toString(),
                      "type error for " + u.op);
            }
            else throw new IllegalArgumentException("reached unary else");
            return;
        }
        //
        
        throw new IllegalArgumentException("should never reach here");
    }

    public static void V (Statement s, TypeMap tm) { //statement가 올바른지 확인하는 함수
        if ( s == null )
            throw new IllegalArgumentException( "AST error: null statement");
        if (s instanceof Skip) return; //skip이면 아무것도 하지않고 함수 종료
        if (s instanceof Assignment) { //만약 할당을하는 문장이라면 
            Assignment a = (Assignment)s;
            check( tm.containsKey(a.target) //먼저 tm의 map에 target의 이름을 가진 변수가 있는지 check함수를 통해 검사
                   , " undefined target in assignment: " + a.target);
            V(a.source, tm); //expression이 올바른지 확인하는 위의 V함수를 이용해 확인
            Type ttype = (Type)tm.get(a.target); //tm의 map에서 target의 타입을 ttype변수에 저장
            Type srctype = typeOf(a.source, tm, functionMap); //exprssion의 객체를 typeof함수를 이용해 그 타입을 srctype에 저장
            if (ttype.toString() != srctype.toString()) { //만약 2개의 타입이 다르다면
                if (ttype.toString() == Type.FLOAT.toString()) //근데 float와 int의 상호호환은 가능 > check함수이용
                    check( srctype.toString() == Type.INT.toString()
                           , "mixed mode assignment to " + a.target);
                else if (ttype.toString() == Type.INT.toString()) //근데  int와 char의 상호호완은 가능
                    check( srctype.toString() == Type.CHAR.toString()
                           , "mixed mode assignment to " + a.target);
                else //위 2개의 경우말고는 오류가 발생
                    check( false
                           , "mixed mode assignment to " + a.target);
            }
            return;
        } 
 
        // student exercise
        if (s instanceof Conditional) { //만약 if문이라면
            Conditional c = (Conditional) s;
            V(c.test, tm); //c의 test를 exprssion을 검사하는 V함수에 전달
            check(typeOf(c.test, tm, functionMap)==Type.BOOL, //그리고 test의 타입이 bool인지 check함수를 이용해 검사
                  "conditional test must return bool in " + c.test);
            V(c.thenbranch, tm); //재귀함수를 이용해 then과 else statement검사
            V(c.elsebranch, tm);
            return;
        }
        if (s instanceof Loop) { //만약 반복문이라면
            Loop l = (Loop) s;
            V(l.test, tm); //l의 test를 exprssion을 검사하는 V함수에 전달
            check(typeOf(l.test, tm, functionMap)==Type.BOOL, // 그리고 test의 타입이 bool인지 check함수를 이용해 검사
                  "loop test must return bool in " + l.test);
            V(l.body, tm); //재귀함수를 이용해 body부분 검사
            return;
        }
        if (s instanceof Block) {  //만약 블록이라면
            Block b = (Block) s;
            if (b.members.size() == 0) return; //블록안에 statement객체가 없으면 그냥 종료
            for (int i = 0; i < b.members.size(); i++) { //statement객체를 재귀함수를 이용해 검사
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
        Parser parser  = new Parser(new Lexer(args[0])); //lexer와 parser결과 저장하는 변수
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

