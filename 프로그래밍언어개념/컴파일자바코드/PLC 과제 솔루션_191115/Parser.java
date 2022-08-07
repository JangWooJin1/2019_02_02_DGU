import java.io.IOException;
import java.util.*;

public class Parser {
    // Recursive descent parser that inputs a C++Lite program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.
  
    Token token;          // current token from the input stream
    Lexer lexer;
  
    String returnplace;
    public Parser(Lexer ts) { // Open the C++Lite source program
        lexer = ts;                          // as a token stream, and
        token = lexer.next();            // retrieve its first Token
    }
  
    private String match (TokenType t) { //해당 토큰이 맞는지 체크하고 맞으면 다음 토큰으로 넘어감 아니면 오류 발생
        String value = token.value(); //String리턴 타입의 value리턴
        if (token.type().equals(t)) 
            token = lexer.next(); //다음 토큰으로 넘어가기
        else
            error(t);	//틀리면 error함수 실행
        return value;
    }
   
    
    private void error(TokenType tok) { //에러 메세지 뜨게하는 함수
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    private void error(String tok) { //에러 메세지 뜨게하는 함수
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
    
    public Program program() {
    	// Program -> { Type Identifier FunctionOrGlobal } MainFuction
    	Functions fs = new Functions(); //FunctionOrGlobal들을 저장하는 배열 객체 생성
    	Declarations g = new Declarations();
    	
    	while(!(token.type().equals(TokenType.Eof))) { //EoF가 나올때까지 반복
    		Type T = new Type(match(token.type()));
    		String id;
    		
    		if(token.type().equals(TokenType.Main)) { //mainfunction
    			id = match(TokenType.Main);
                fs.add(mainfunction(T, id));
    		}
    		else if(token.type().equals(TokenType.Identifier)){
    			id = match(TokenType.Identifier);
    			if (token.type().equals(TokenType.LeftParen)) { //function
    				fs.add(function(T, id)); 
    			}
    			else  { // Global
    				globals(g,T,id);
    			}
    		}
        	else {
        		error("FuctionOrGlobal error");
        	}
    	}
    	
    	
    	return new Program(g, fs);
    }
        
    
    public void globals(Declarations g, Type t, String id){
    	//Global -> {, Identifier }
        Variable V = new Variable(id);    
    	g.add(new Declaration(V,t)); //declartion 클래스의 v,t에 값을 넣어 객체를 만든후 그걸 declations의 D배열에 추가
    	
    	while(token.type().equals(TokenType.Comma)) { //만약 ','가 존재하면 검사 더하기
    		match(TokenType.Comma);
    		V = new Variable(match(TokenType.Identifier));
    		g.add(new Declaration(V,t));
    	}
    	
    	match(TokenType.Semicolon); //문장 마지막에 ';'검사
    	
    }
    
    
    public Function function(Type t, String id){
    	returnplace = id;
    	match(TokenType.LeftParen);
        Declarations params = parameter();
        match(TokenType.RightParen);
        match(TokenType.LeftBrace);
        Declarations locals = declarations();
        Block body = statements();
        match(TokenType.RightBrace);
        return new Function(t,id,params,locals,body); 
    }
    
    public Function mainfunction(Type t, String id) {
    	// MainFuction --> int main ( ) '{' Declarations Statements '}'
    	
    	TokenType[ ] header = {TokenType.LeftParen, TokenType.RightParen};
        for (int i=0; i<header.length; i++)   // bypass "int main ( )"
            match(header[i]);
      
        // student exercise
        match(TokenType.LeftBrace); // '{'가 있는지 match함수를 통해 검사
        Declarations params = new Declarations();
        Declarations locals = declarations();
        Block body = statements();
        match(TokenType.RightBrace); // '}'가 있는지 match함수를 통해 검사
        return new Function(t,id,params,locals,body);  // student exercise
    }
  
    
    public Declarations parameter() {
    	Declarations P=new Declarations();
    	if(isType()) {
    		Type T = new Type(match(token.type())); //Type이 있으면 T에다 넣어주기
    		Variable V = new Variable(match(TokenType.Identifier)); //V에다 identifier값 넣어주기
        	P.add(new Declaration(V,T)); //declartion 클래스의 v,t에 값을 넣어 객체를 만든후 그걸 declations의 D배열에 추가
        	
        	while(token.type().equals(TokenType.Comma)) { //만약 ','가 존재하면 검사 더하기
        		match(TokenType.Comma);
        		T = new Type(match(token.type()));
        		V = new Variable(match(TokenType.Identifier));
        		P.add(new Declaration(V,T));
        	}
    		
    		
    	}
    	else {	error("Parameters error");	} //파라메타가 존재하지 않는 경우
    	
    	return P;
    	
    }
    
    
    
    private Declarations declarations () {
        // Declarations --> { Declaration }
    	Declarations D = new Declarations();
    	while(isType()) { //type이 없을 때까지 반복
    		declaration(D);
    	}
        return D;  // student exercise
    }
  
    private void declaration (Declarations ds) {
        // Declaration  --> Type Identifier { , Identifier } ;
        // student exercise
    	Type T = null;
    	if(isType()) {
    		T = new Type(match(token.type())); //Type이 있으면 T에다 넣어주기
    	}
    	else {	error("declartion-type");	} //없으면 type에 관한 에러라고 출력
    	
    	Variable V = new Variable(match(TokenType.Identifier)); //V에다 identifier값 넣어주기
    	ds.add(new Declaration(V,T)); //declartion 클래스의 v,t에 값을 넣어 객체를 만든후 그걸 declations의 D배열에 추가
    	
    	while(token.type().equals(TokenType.Comma)) { //만약 ','가 존재하면 검사 더하기
    		match(TokenType.Comma);
    		V = new Variable(match(TokenType.Identifier));
    		ds.add(new Declaration(V,T));
    	}
    	
    	match(TokenType.Semicolon); //문장 마지막에 ';'검사
    	
    }

  
    private Type type() {
        // Type  -->  int | bool | float | char 
        Type t = null;
        // student exercise
        t = new Type(match(token.type())); //type검사를 하고 Type클래스 id에 값 전달
        return t;          
    }
  
    
    private Block statements () {
        // Block --> '{' Statements '}'
        Block b = new Block();
        // student exercise
       while(!token.type().equals(TokenType.RightBrace)) { //블럭이 끝날때까지 반복하기
    	   b.members.add(statement()); //statement()함수를 실행하고 그 반환값을 ArrayList<Statement> members에 추가
       }
        return b;
    }
    
    
    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement  
    	
    	//| CallStatement | ReturnStatement

        Statement s = new Skip();
		
        // student exercise
        if (token.type().equals(TokenType.Semicolon)) { //';'일때 검사
            match(TokenType.Semicolon);
        }
        else if (token.type().equals(TokenType.Return)) { 
            s = Returnstatement(); 
        }
        else if (token.type().equals(TokenType.LeftBrace)) { // 만약 블록 '{'이 나왔을 때 검사
            match(TokenType.LeftBrace);
            s = statements(); //statements()함수를 실행하고 그결과를 s에 저장
            match(TokenType.RightBrace); // '}' 검사
        }
        
        else if (token.type().equals(TokenType.Identifier)) { // 만약 변수이름이 나왔을 때 검사
        	Variable V =new Variable(match(TokenType.Identifier)); //함수호출인지 값 할당인지 구분하기 위해 assignment의 match(Identifier)을 여기서 함)
        	
        	if(token.type().equals(TokenType.Assign)) {
        		s = assignment(V); //assignment()함수를 실행하고 그결과를 s에 저장
        	}
        	else if(token.type().equals(TokenType.LeftParen)) {
        		s = callstatement(V);
        	}
        }
        else if (token.type().equals(TokenType.If)) { // 만약 'if'가 나왔을 때 검사
            s = ifStatement(); //ifStatement()함수를 실행하고 그결과를 s에 저장
        }
        else if (token.type().equals(TokenType.While)) { // 만약 'while'가 나왔을 때 검사
            s = whileStatement(); //whileStatement()함수를 실행하고 그결과를 s에 저장
        }
        else {
            error("statement"); //만약 다 아니면 statement에러라고 출력
        }
        return s;
    }
  
    private Callstatement callstatement(Variable V) {
    	
    	String N = V.toString();
    	match(TokenType.LeftParen);
    	Expressions A = new Expressions();
    	
    	
    	while(!(token.type().equals(TokenType.RightParen))){
            A.add(expression());
            if (!token.type().equals(TokenType.RightParen))
                match(TokenType.Comma);
        }
    	
    	
    	match(TokenType.RightParen);
    	match(TokenType.Semicolon);
    	return new Callstatement(N,A);
    }
    
    private Return Returnstatement() {
    	match(TokenType.Return);
    	Expression r= expression();
    	match(TokenType.Semicolon);
    	return new Return(new Variable(returnplace),r);
    }
    
    private Assignment assignment (Variable V) {
        // Assignment --> Identifier = Expression ;
		match(TokenType.Assign); //그다음 '=' 이 있는지 검사
		Expression E = expression(); //그다음 expression() 함수를 실행
		match(TokenType.Semicolon); //마지막에 ';'검사
		
		Assignment A = new Assignment(V,E); //그리고 assignment 클래스의 target,source에 값 전달
        return A;  // student exercise
    }
  
    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
    	match(TokenType.If); //먼저 'if'가 있는지 검사
		match(TokenType.LeftParen); //그다음 '('이 있는지 검사
        Expression e = expression(); //그다음 expression검사
        match(TokenType.RightParen); // 그 다음 ')'검사

		Statement S = statement();  //그 다음 statement검사
		
		Conditional C; //conditional 클래스에 대한 객체를 만든후 조건에 따라 오버로딩을 이용해 생성자 호출
		if(token.type().equals(TokenType.Else)){ //else가 존재할 때
			match(TokenType.Else);
			
			Statement S2 = statement();  //else의 statement까지 검사후
			
			C = new Conditional(e,S,S2);	//conditional클래스의 test,thenbranch,elsebranch에 값 전달
		}
		else { C = new Conditional(e,S); } //else가 없으면 그냥 test,thenbranch에만 값 전달

        return C;  // student exercise
    }
  
    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
    	match(TokenType.While); //먼저 while이 있는지 검사
		match(TokenType.LeftParen); //그다음 '('이 있는지 검사
        Expression e = expression(); //그다음 expression검사
        match(TokenType.RightParen); // 그 다음 ')'검사

    	Statement S = statement();  //그 다음 statement검사
	    	
    	Loop L = new Loop(e,S); //loop클래스의 test와 body에 값 전달

        return L;  // student exercise
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
    	Expression e = conjunction(); //conjunction함수의 실행결과를 저장
    	while(token.type().equals(TokenType.Or)) { //만약 '||'이 있을 때 검사
    		 Operator op = new Operator(match(TokenType.Or)); //operator클래스의 val에 값 전달
             Expression term2 = conjunction(); //두번째 conjunction함수 실행결과 저장
             e = new Binary(op, e, term2); //Binary클래스의 op,term1, term2에 값 전달
    	}
        return e;  // student exercise
 
    }
  
    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
    	Expression e = equality(); //equality함수의 실행결과를 저장
    	while(token.type().equals(TokenType.And)) { //만약 '&&'이 있을 때 검사
    		 Operator op = new Operator(match(TokenType.And)); //operator클래스의 val에 값 전달
             Expression term2 = equality(); //두번째 equality함수의 실행결과를 저장
             e = new Binary(op, e, term2); //Binary클래스의 op,term1, term2에 값 전달
    	}
        return e;  // student exercise
    }
  
    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
    	Expression e = relation(); //relation함수의 실행결과를 저장
    	while(isEqualityOp()) { //만약 동등비교연산자(==)가 있을 때 검사
    		 Operator op = new Operator(match(TokenType.Equals)); //operator클래스의 val에 값 전달
             Expression term2 = relation(); //두번째 relation함수의 실행결과를 저장
             e = new Binary(op, e, term2); //Binary클래스의 op,term1, term2에 값 전달
    	}
        return e;  // student exercise
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition] 
    	Expression e = addition(); //addition함수의 실행결과를 저장
    	while(isRelationalOp()) { //만약 비교연산자(>)가 있을 때 검사
    		Operator op = new Operator(match(token.type())); //operator클래스의 val에 값 전달
    		Expression term2 = addition(); //두번째 addition함수의 실행결과를 저장
    		e = new Binary(op,e,term2); //Binary클래스의 op,term1, term2에 값 전달
    	} 
        return e;  // student exercise
    }
  
    private Expression addition () {
        // Addition --> Term { AddOp Term }
        Expression e = term(); //term함수의 실행결과를 저장
        while (isAddOp()) { //만약 증감연산자(+)가 있을 때 검사
            Operator op = new Operator(match(token.type())); //operator클래스의 val에 값 전달
            Expression term2 = term(); //두번째 term함수의 실행결과를 저장
            e = new Binary(op, e, term2); //Binary클래스의 op,term1, term2에 값 전달
        }
        return e;
    }
  
    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        Expression e = factor(); //factor함수의 실행결과를 저장
        while (isMultiplyOp()) { //만약 곱셈,나눗셈 연산자가 있을 때 검사
            Operator op = new Operator(match(token.type())); //operator클래스의 val에 값 전달
            Expression term2 = factor(); //두번째 factor함수의 실행결과를 저장
            e = new Binary(op, e, term2); //Binary클래스의 op,term1, term2에 값 전달
        }
        return e;
    }
  
    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary 
        if (isUnaryOp()) { //만약 '!'와 '-'가 있으면
            Operator op = new Operator(match(token.type())); //operator클래스의 val에 값 전달
            
            Expression term = primary(); //primary함수 실행결과 저장
            return new Unary(op, term); //Unary클래스의 op,term에 값 전달
        }
        else return primary(); //없다면 그냥 primary함수 실행결과를 리턴
    }
  
    private Expression primary () {
        // Primary --> Identifier | Literal | ( Expression )
        //             | Type ( Expression )
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) { //만약 변수 이름이 있을 때
        	String n = match(TokenType.Identifier); //변수이름 저장
        
        	if(token.type().equals(TokenType.LeftParen)){
                match(TokenType.LeftParen);
                Expressions args = new Expressions();
                while(!(token.type().equals(TokenType.RightParen))){
                    args.add(expression());
                    if (!token.type().equals(TokenType.RightParen))
                        match(TokenType.Comma);
                }
                match(TokenType.RightParen);
                e = new Call(n,args);
        	}
        	else {
                e = new Variable(n);
            }
        
        
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) { //만약 '('이 있을 때
            token = lexer.next(); //match가 없기 때문에 next()를 이용해 token을 다음걸로 넘기기
            e = expression();      //expression함수 결과 저장 
            match(TokenType.RightParen); // ')'검사
        } else if (isType( )) { //만약 type이 있을 때 검사(형변환)
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen); //'(' 검사
            Expression term = expression(); //expression함수 결과 저장
            match(TokenType.RightParen); //')' 검사
            e = new Unary(op, term); //Unary클래스의 op,term에 값 전달
        } else error("Identifier | Literal | ( | Type"); //아무것도 아닐 때 에러 메세지 출력
        return e;
    }

    private Value literal( ) {
    	Value V = null;
    	if(token.type().equals(TokenType.IntLiteral)) { //int의 값일 때
    		V = new IntValue(Integer.parseInt(match(TokenType.IntLiteral)));
    	}
    	else if(token.type().equals(TokenType.FloatLiteral)) { //Float의 값일 때
    		V = new FloatValue(Float.valueOf(match(TokenType.FloatLiteral)));
    	}
    	else if(token.type().equals(TokenType.CharLiteral)) { //Char의 값일 때
    		V = new CharValue(match(TokenType.CharLiteral).charAt(0));
    	}
    	else if(token.type().equals(TokenType.Bool)) { //bool연산이 있을 때
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.IntLiteral)));
    	}
    	else if(token.type().equals(TokenType.True)) { //true값이 있을 때
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.True)));
    	}
    	else if(token.type().equals(TokenType.False)) { //false값이 있을 때
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.False)));
    	}
        return V;  // student exercise
    }
  

    private boolean isAddOp( ) { // +,-연산자를 리턴
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) { // *,/ 연산자를 리턴
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }
    
    private boolean isUnaryOp( ) { // !, -(부호) 연산자를 리턴
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isEqualityOp( ) { // ==, != 연산자를 리턴
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) { // <, <=, >, >= 연산자를 리턴
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) { // int, float, char, bool 타입을 리턴
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char);
    }
    
    private boolean isLiteral( ) {  //int,float,char의 값과 isBooleanLiteral() 을 리턴
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }
    
    private boolean isBooleanLiteral( ) { //true와 false값 리턴
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }
    
    public static void main(String args[]) throws IOException {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
		prog.display(args[0]);
		
    } //main

} // Parser
