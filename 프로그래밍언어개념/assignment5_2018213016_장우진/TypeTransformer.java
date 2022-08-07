import java.io.IOException;
import java.util.*;

public class TypeTransformer {

	private static Type returnType;
    private static boolean returnFound = false;
    private static TypeMap functionMap = new TypeMap();
    private static Functions dtFunction = new Functions();
	
    public static Program T (Program p, TypeMap tm) {
    	 dtFunction.addAll(p.functions);
         //10.1
         Declarations ds = new Declarations();
         ds.addAll(p.global);
         for (int i=0; i<p.functions.size(); i++) {
             Variable fl = new Variable(p.functions.get(i).id);
             ds.add(new Declaration(fl, p.functions.get(i).t));
             functionMap.put(fl, p.functions.get(i).t);
             
         }
         T(ds);
         //10.2
         for (Function func : p.functions){
             T(func);
         }
         //10.3
         Functions NF = new Functions();
         NF = T(p.functions,tm);
         
         return new Program(p.global, NF);
    } 
    
    public static void T (Function f){
        Declarations ds = new Declarations();
        ds.addAll(f.params);
        ds.addAll(f.local);
        T(ds); 
    }
    
    public static Functions T(Functions f, TypeMap tm) {
        Functions NF = new Functions();
        for (Function func : f) {
            TypeMap fMap = new TypeMap();
            fMap.putAll(tm);
            fMap.putAll(StaticTypeCheck.typing(func.params));
            fMap.putAll(StaticTypeCheck.typing(func.local));
            
            NF.add(T(func, fMap));
        }
        return NF;
    }

    
    public static Function T(Function f, TypeMap tm){
        
        returnType = f.t;
        returnFound = false;
        Block b = (Block)T(f.body,tm);
        Function NF = new Function(f.t,f.id,f.params,f.local,b);

        //10.4
        if (returnType.toString()!=Type.VOID.toString() && !f.id.equals("main")){
            StaticTypeCheck.check((returnFound == true),
                f.id + " is a non-Void function with no Return Statement");
        }
        return NF;
    }    
    
    public static void T(Declarations d) {
        for (int i=0; i<d.size() - 1; i++)
            for (int j=i+1; j<d.size(); j++) {
                Declaration di = d.get(i);
                Declaration dj = d.get(j);
                StaticTypeCheck.check( ! (di.v.equals(dj.v)),
                       "duplicate declaration: " + dj.v);
                //System.out.println(di.v + " = " + dj.v);
            }
    }
    
    
    public static Expression T (Expression e, TypeMap tm) {
        if (e instanceof Value) 
            return e;
        if (e instanceof Variable) 
            return e;
        
        if (e instanceof Call){
            Call c = (Call)e;
            StaticTypeCheck.check(functionMap.get(new Variable(c.name)).toString() != Type.VOID.toString(),
                    "Expression Calls must have a return type.");
            for (Function func : dtFunction){
                if (func.id.equals(c.name)){
                    StaticTypeCheck.check (c.args.size() == func.params.size(),
                            "Arguments and Parameters are different size.");
                    
                    for(int i = 0; i < c.args.size(); i++){
                        Type ti = ((Type)func.params.get(i).t);
                        Type tj = StaticTypeCheck.typeOf(c.args.get(i),tm, functionMap); 
                        StaticTypeCheck.check(ti.toString()==tj.toString()
                                , func.params.get(i).t + " is not equal to " + StaticTypeCheck.typeOf(c.args.get(i),tm, functionMap));
                    }
                }
                return c;
            }
        }
        
        if (e instanceof Binary) {
            Binary b = (Binary)e; 
            Type typ1 = StaticTypeCheck.typeOf(b.term1, tm,functionMap);
            Type typ2 = StaticTypeCheck.typeOf(b.term2, tm,functionMap);
            Expression t1 = T (b.term1, tm);
            Expression t2 = T (b.term2, tm);
            if (typ1.toString() == Type.INT.toString()) 
                return new Binary(b.op.intMap(b.op.val), t1,t2);
            else if (typ1.toString() == Type.FLOAT.toString()) 
                return new Binary(b.op.floatMap(b.op.val), t1,t2);
            else if (typ1.toString() == Type.CHAR.toString()) 
                return new Binary(b.op.charMap(b.op.val), t1,t2);
            else if (typ1.toString() == Type.BOOL.toString()) 
                return new Binary(b.op.boolMap(b.op.val), t1,t2);
            throw new IllegalArgumentException("should never reach here");
        }
        
        // student exercise
        if (e instanceof Unary) {
            Unary u = (Unary) e;
            Type typ = StaticTypeCheck.typeOf(u.term, tm,functionMap);
            Expression t = T (u.term, tm);
            if (typ.toString()==Type.INT.toString())
                return new Unary(u.op.intMap(u.op.val), t);
            else if (typ.toString()==Type.FLOAT.toString()) 
                return new Unary(u.op.floatMap(u.op.val), t);
            else if (typ.toString()==Type.CHAR.toString())
                return new Unary(u.op.charMap(u.op.val), t);
            else if (typ.toString()==Type.BOOL.toString())
                return new Unary(u.op.boolMap(u.op.val), t);
            throw new IllegalArgumentException("should not reach here");
        }
        //
        throw new IllegalArgumentException("should never reach here");
    }

    public static Statement T (Statement s, TypeMap tm) {
        if (s instanceof Skip) return s;
        if (s instanceof Assignment) {
            Assignment a = (Assignment)s;
            Variable target = a.target;
            Expression src = T (a.source, tm);
            Type ttype = (Type)tm.get(a.target);
            Type srctype = StaticTypeCheck.typeOf(a.source, tm, functionMap);
            if (ttype.toString() == Type.FLOAT.toString()) {
                if (srctype.toString() == Type.INT.toString()) {
                    src = new Unary(new Operator(Operator.I2F), src);
                    srctype = Type.FLOAT;
                }
            }
            else if (ttype.toString() == Type.INT.toString()) {
                if (srctype.toString() == Type.CHAR.toString()) {
                    src = new Unary(new Operator(Operator.C2I), src);
                    srctype = Type.INT;
                }
            }
            StaticTypeCheck.check( ttype.toString() == srctype.toString(),
                      "bug in assignment to " + target);
            return new Assignment(target, src);
        } 
        if (s instanceof Conditional) {
            Conditional c = (Conditional)s;
            Expression test = T (c.test, tm);
            Statement tbr = T (c.thenbranch, tm);
            Statement ebr = T (c.elsebranch, tm);
            return new Conditional(test,  tbr, ebr);
        }
        if (s instanceof Loop) {
            Loop l = (Loop)s;
            Expression test = T (l.test, tm);
            Statement body = T (l.body, tm);
            return new Loop(test, body);
        }
        if (s instanceof Block) {
            Block b = (Block)s;
            Block out = new Block();
            for (Statement stmt : b.members)
                out.members.add(T(stmt, tm));
            return out;
        }
        
        if (s instanceof Return)
        {
            //10.5
            StaticTypeCheck.check(returnType.toString()!=Type.VOID.toString(),    
                "Return is not a valid Statement in a Void Function");
            Return r = (Return)s;
            //10.4
            Return q = new Return(r.target,T(r.result,tm));
            StaticTypeCheck.check(returnType.toString()==StaticTypeCheck.typeOf(q.result,tm, functionMap).toString(),
                "The returned type does not match the fuction type;");
            returnFound = true;
            return q;
        }
        if (s instanceof Callstatement){
        	Callstatement c = (Callstatement)s;
            StaticTypeCheck.check((functionMap.get(new Variable(c.name))).toString() == Type.VOID.toString(),
                    "Statement Calls can only be to Void statements");
            for (Function func : dtFunction){
                if (func.id.equals(c.name)){
                    StaticTypeCheck.check (c.args.size() == func.params.size(),
                            "Arguments and Parameters are different size.");
                    for(int i = 0; i < c.args.size(); i++){
                        Type ti =((Type)func.params.get(i).t);
                        Type tj = StaticTypeCheck.typeOf(c.args.get(i),tm, functionMap); 
                        StaticTypeCheck.check(ti.toString()==tj.toString()
                                , func.params.get(i).t + " is not equal to " + StaticTypeCheck.typeOf(c.args.get(i),tm, functionMap));
                    }
                }
            }
            return c;
        }
        
        throw new IllegalArgumentException("should never reach here");
    }
    

    public static void main(String args[]) throws IOException {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        prog.display(args[0]);           // student exercise
        System.out.println("\nBegin type checking...");
        System.out.println("Type map:");
        
        
        Declarations ds = new Declarations();
        ds.addAll(prog.global);
        
        for (int i=0; i<prog.functions.size(); i++) { 
            Variable fl = new Variable(prog.functions.get(i).id);
            ds.add(new Declaration(fl,prog.functions.get(i).t));
            functionMap.put(fl, prog.functions.get(i).t);
        }
        
        TypeMap map = StaticTypeCheck.typing(ds);
           // student exercise
        StaticTypeCheck.V(prog);
        Program out = T(prog, map);
        System.out.println("Output AST");
        out.display(args[0]);    // student exercise
    } //main

    } // class TypeTransformer

    
