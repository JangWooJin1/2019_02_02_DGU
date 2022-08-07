import java.io.IOException;

// Following is the semantics class:
// The meaning M of a Statement is a State
// The meaning M of a Expression is a Value

public class Semantics {

    State M (Program p) { 
    	Function f = p.functions.get("main");
		State mainstate = new State();
		
		for (Declaration decl : f.local)
			mainstate.put(decl.v, Value.mkValue(decl.t));
		for (Declaration decl : p.global)
			mainstate.put(decl.v, Value.mkValue(decl.t));
	
		System.out.println("Entering main : ");
		System.out.println(mainstate.display());
		
        return M(p.functions.get("main").body, p.functions, initialState(p.global));
    }
  
    State initialState (Declarations d) {
        State state = new State();
        Value intUndef = new IntValue();
        for (Declaration decl : d)
            state.put(decl.v, Value.mkValue(decl.t));
        return state;
    }
    
    
    
 
  
    State M (Statement s, Functions fs, State state) {
        if (s instanceof Skip) return M((Skip)s, state);
        if (s instanceof Assignment)  return M((Assignment)s, fs,state);
        if (s instanceof Conditional)  return M((Conditional)s,fs, state);
        if (s instanceof Loop)  return M((Loop)s,fs, state);
        if (s instanceof Block)  return M((Block)s,fs, state);
        if (s instanceof Callstatement)	return M((Callstatement)s, fs, state);
        throw new IllegalArgumentException("should never reach here");
    }
  
    State M (Skip s, State state) {
        return state;
    }
  
    State M (Assignment a, Functions fs ,State state) {
        return state.onion(a.target, M (a.source, fs ,state));
    }
  
    State M (Block b, Functions fs, State state) {
        for (Statement s : b.members) {
            state = M (s, fs , state);
        }
        return state;
    }
  
    State M (Conditional c, Functions fs ,State state) {
        if (M(c.test, fs, state).boolValue( ))
            return M (c.thenbranch, fs, state);
        else
            return M (c.elsebranch, fs, state);
    }
  
    State M (Loop l, Functions fs ,State state) {
        if (M (l.test, fs, state).boolValue( ))
            return M(l, fs, M (l.body, fs ,state));
        else return state;
    }


    State M(Callstatement c, Functions fs, State state) {
    			//call한 함수를 찾기
    			Function f = fs.get(c.name.toString());
    			
    			//함수의 지역변수를 state에 추가
    			for (Declaration decl : f.local)
    				state.put(decl.v, Value.mkValue(decl.t));

    			// 파라메타 state에 추가
    			Expressions argIt = c.args;
    			Declarations funcIt = f.params; 
   
    			for(int i=0; i<argIt.size(); i++) {
    				Expression exp = argIt.get(i);
    				Declaration dec = funcIt.get(i);
    				Value v = M(exp, fs, state);
    				state.put(dec.v, v);
    			}
    			System.out.println("calling : "+f.id);
    			System.out.println(state.display());
    			

    			//함수의 body 정보
    			Block members = f.body;
    			
    			for(int i=0; i<members.members.size(); i++) { //함수 statement하나하나 검사
    				Statement s = members.members.get(i);
    					
    				state = M(s, fs, state);
    			}
    			
    			//state에서 지역변수와 파라메타 제거
    			System.out.println("returning : "+f.id);
				System.out.println(state.display());
    			for (Declaration decl : f.local)
    				state.remove(decl.v);
    			for (Declaration decl : f.params)
    				state.remove(decl.v);
    			return state;
    }
    
    Value applyBinary (Operator op, Value v1, Value v2) {
        StaticTypeCheck.check( ! v1.isUndef( ) && ! v2.isUndef( ),
               "reference to undef value");
        if (op.val.equals(Operator.INT_PLUS)) 
            return new IntValue(v1.intValue( ) + v2.intValue( ));
        if (op.val.equals(Operator.INT_MINUS)) 
            return new IntValue(v1.intValue( ) - v2.intValue( ));
        if (op.val.equals(Operator.INT_TIMES)) 
            return new IntValue(v1.intValue( ) * v2.intValue( ));
        if (op.val.equals(Operator.INT_DIV)) 
            return new IntValue(v1.intValue( ) / v2.intValue( ));
        // student exercise
        if (op.val.equals(Operator.INT_LT)) 
            return new BoolValue(v1.intValue( ) < v2.intValue( ));
        if (op.val.equals(Operator.INT_LE)) 
            return new BoolValue(v1.intValue( ) <= v2.intValue( ));
        if (op.val.equals(Operator.INT_EQ)) 
            return new BoolValue(v1.intValue( ) == v2.intValue( ));
        if (op.val.equals(Operator.INT_NE)) 
            return new BoolValue(v1.intValue( ) != v2.intValue( ));
        if (op.val.equals(Operator.INT_GT)) 
            return new BoolValue(v1.intValue( ) > v2.intValue( ));
        if (op.val.equals(Operator.INT_GE)) 
            return new BoolValue(v1.intValue( ) >= v2.intValue( ));
        // FLOAT
        if (op.val.equals(Operator.FLOAT_PLUS)) 
            return new FloatValue(v1.floatValue( ) + v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_MINUS)) 
            return new FloatValue(v1.floatValue( ) - v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_TIMES)) 
            return new FloatValue(v1.floatValue( ) * v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_DIV)) 
            return new FloatValue(v1.floatValue( ) / v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_LT)) 
            return new BoolValue(v1.floatValue( ) < v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_LE)) 
            return new BoolValue(v1.floatValue( ) <= v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_EQ)) 
            return new BoolValue(v1.floatValue( ) == v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_NE)) 
            return new BoolValue(v1.floatValue( ) != v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_GT)) 
            return new BoolValue(v1.floatValue( ) > v2.floatValue( ));
        if (op.val.equals(Operator.FLOAT_GE)) 
            return new BoolValue(v1.floatValue( ) >= v2.floatValue( ));
        // CHAR
        if (op.val.equals(Operator.CHAR_LT)) 
            return new BoolValue(v1.charValue( ) < v2.charValue( ));
        if (op.val.equals(Operator.CHAR_LE)) 
            return new BoolValue(v1.charValue( ) <= v2.charValue( ));
        if (op.val.equals(Operator.CHAR_EQ)) 
            return new BoolValue(v1.charValue( ) == v2.charValue( ));
        if (op.val.equals(Operator.CHAR_NE)) 
            return new BoolValue(v1.charValue( ) != v2.charValue( ));
        if (op.val.equals(Operator.CHAR_GT)) 
            return new BoolValue(v1.charValue( ) > v2.charValue( ));
        if (op.val.equals(Operator.CHAR_GE)) 
            return new BoolValue(v1.charValue( ) >= v2.charValue( ));
        //BOOL
        if (op.val.equals(Operator.AND))
            return new BoolValue(v1.boolValue() && v2.boolValue());
        if (op.val.equals(Operator.OR))
            return new BoolValue(v1.boolValue() || v2.boolValue());
        if (op.val.equals(Operator.BOOL_EQ))
            return new BoolValue(v1.boolValue() == v2.boolValue());
        if (op.val.equals(Operator.BOOL_NE))
            return new BoolValue(v1.boolValue() != v2.boolValue());
        //
        throw new IllegalArgumentException("should never reach here");
    } 
    
    Value applyUnary (Operator op, Value v) {
        StaticTypeCheck.check( ! v.isUndef( ),
               "reference to undef value");
        
        if (op.val.equals(Operator.NOT))
            return new BoolValue(!v.boolValue( ));
        else if (op.val.equals(Operator.INT_NEG))
            return new IntValue(-v.intValue( ));
        else if (op.val.equals(Operator.FLOAT_NEG))
            return new FloatValue(-v.floatValue( ));
        else if (op.val.equals(Operator.I2F))
            return new FloatValue((float)(v.intValue( ))); 
        else if (op.val.equals(Operator.F2I))
            return new IntValue((int)(v.floatValue( )));
        else if (op.val.equals(Operator.C2I))
            return new IntValue((int)(v.charValue( )));
        else if (op.val.equals(Operator.I2C))
            return new CharValue((char)(v.intValue( )));
        throw new IllegalArgumentException("should never reach here");
    } 

    Value M (Expression e, Functions fs ,State state) {
        if (e instanceof Value) 
            return (Value)e;
        if (e instanceof Variable) 
            return (Value)(state.get(e));
        if(e instanceof Call)
			return CallExpression((Call)e, fs, state);
        if (e instanceof Binary) {
            Binary b = (Binary)e;
            return applyBinary (b.op, 
                                M(b.term1, fs, state), M(b.term2, fs, state));
        }
        if (e instanceof Unary) {
            Unary u = (Unary)e;
            return applyUnary(u.op, M(u.term, fs, state));
        }
        throw new IllegalArgumentException("should never reach here");
    }
    
    Value CallExpression(Call c, Functions fs, State state){
		//call 한 함수 찾기
		Function f = fs.get(c.name);
		
		// 함수의 지역변수 state에 추가
		for (Declaration decl : f.local) {
			state.put(decl.v, Value.mkValue(decl.t));
		}

		// 함수의 파라메타 state에 추가
		Expressions argIt = c.args;
		Declarations funcIt = f.params; 
		
		for(int i=0; i<argIt.size(); i++) {
			Expression exp = argIt.get(i);
			Declaration dec = funcIt.get(i);
			Value v = M(exp, fs, state);
			state.put(dec.v, v);
		}
		
		System.out.println("calling : "+f.id);
		System.out.println(state.display());
		
		// 함수 body 저장
		Block members = f.body;
		for(int i=0; i<members.members.size(); i++) { //함수 statement에 대한 state변화 검사
			Statement s = members.members.get(i);
			
			
			if(s instanceof Return){ //return이 나왔을때
			
				Value v =  M(((Return)s).result, fs, state);
				System.out.println("returning : "+f.id);
				System.out.println(state.display());
				
				//state에서 함수 지역변수와 파라메타 제거
				for (Declaration decl : f.local)
					state.remove(decl.v);
				for (Declaration decl : f.params)
					state.remove(decl.v);
				return v;
			} 
			
			
			else { //return을 제외한 나머지 statement
				state = M(s, fs, state);
			}
		}
		
		throw new IllegalArgumentException("attemped to interpret function call with no return as expression");
	}
    
  
    public static void main(String args[]) throws IOException {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        prog.display(args[0]);    // student exercise
        System.out.println("\nBegin type checking...");
        System.out.println("Type map:");
      
        StaticTypeCheck.V(prog);
        
        Declarations ds = new Declarations();
        ds.addAll(prog.global);
        
        for (int i=0; i<prog.functions.size(); i++) { //
            Variable fl = new Variable(prog.functions.get(i).id);
            ds.add(new Declaration(fl,prog.functions.get(i).t));
        }
        
        TypeMap map = StaticTypeCheck.typing(ds);
        Program out = TypeTransformer.T(prog, map);
        System.out.println("Output AST");
        out.display(args[0]);    // student exercise
        // student exercise
        //여기부터 새로운 과제
        
        Semantics semantics = new Semantics( );
        State state = semantics.M(out);
        System.out.println("Leaving main : ");
        System.out.println(state.display( ));  // student exercise
        
        System.out.println("Final State");
        System.out.println("{}");
    }
}
