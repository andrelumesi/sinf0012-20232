// 2. Adotando o algoritmo visto em sala de aula, codifique um analisador sintático LL para a seguinte gramática livre de contexto. (2,0)
//  pg  → f DEFfun | c DEFcmd | hyb DEFfun pg | jyb DEFcmd pg
//  DEFcmd → FUNC ID ( parami) ;  |  ID = num ;
//  parami → ID ID  | , ID ID
//  DEFfun → FUNC ID ( parami ) pg

// Codigo apenas para ilustrar questão, o mesmo não demanda de implementações para ser executável, a saber métodos reconhecer, e proxToken

public class Quest02{

   private string tok;
   public void pg(){
      switch(tok){
         case f: proxToken(); DEFfun(); break;
         case c: proxToken(); DEFcmd(); break;
         case hyb: proxToken(); DEFfun(); pg(); break;
         case jyb: proxToken(); DEFcmd(); pg(); break;
         default: erro();
      }
   }

   public void DEFcmd(){
      switch(tok){
         case FUNC: proxToken(); reconhecer(ID); reconhecer("("); parami(); reconhecer(")"); reconhecer(";") ; break;
         case ID: proxToken(); reconhecer("="); reconhecer(num); reconhecer(";"); break;
         default: erro();
      }
   }

   public void parami(){
      switch(tok){
         case ID: proxToken(); reconhecer(ID); break;
         case ",": proxToken(); reconhecer(ID); reconhecer(ID); break;
         default: erro();
      }
   }

   public void DEFfun(){
      switch(tok){
         case FUNC: proxToken(); reconhecer(ID); reconhecer("("); parami(); reconhecer(")"); pg(); break;
         default: erro();
      }
   }
}
