/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraampli;

import java.util.LinkedList;

/**
 *
 * @author Alber
 */
public class Expresion {
    
//Caracter actual
      private  int actual = 0;
   
       //Expresion a evaluar
       private String expresion;

     //Pila de operadores
       private LinkedList<Tipo> pilaOp;
  
      //Pila de Numeros
      private LinkedList<Double> pilaNum;
  
      //Contador de parentesis
      private int parent;
  
     //Inicializa la expresion
      public Expresion(String expresion) {
          this.expresion = expresion;
          pilaOp = new LinkedList<Tipo>();
          pilaNum = new LinkedList<Double>();
      }


      
  
     //Compila la expresion
      public double compile() throws Exception {
          Lexema lexema;
  
          //Lee los lexemas mientras no se llegue al final de la expresion
          while ((lexema = siguiente()).tipo != Tipo.FIN) {
              switch (lexema.tipo) {
                  case NUMERO:
                      //Almacenamos el numero en la pila
                      pilaNum.push(lexema.valor);
                     break;
                  case PAR_IZQ:
                      //Parentesis izquierdo, hay que almacenarlo siempre
                      ++parent;
                      pilaOp.push(lexema.tipo);
                      break;
                  case PAR_DER:
                      /*
                       * Parentesis derecho, hay que terminar de calcular la
                       * expresion hasta que se encuentre el parentesis izquierdo
                       */
                      --parent;
                      Tipo tipo;
  
                      while ((tipo = pilaOp.pop()) != Tipo.PAR_IZQ) {
                          //hace la operacion que indica tipo
                          operacion(tipo);
                      }
  
                      break;
                  default:
                      //Se trata de un operador: + - * /
                      if (pilaOp.size() > 0 && pilaOp.getLast() != Tipo.PAR_IZQ 
                              && pilaOp.getLast().prioridad >= lexema.tipo.prioridad) {
                          //se checa si el operador anterior tiene mayor prioridad
                          operacion(pilaOp.pop());
                      }
  
                      //Se guarda el operador actual
                      pilaOp.push(lexema.tipo);
              }
          }
  
          //Si quedaron operadores en la pila hay que realizar las operaciones
          while (pilaOp.size() > 0) {
              operacion(pilaOp.pop());
          }
  
          //Si parentesis no es cero, hay parentesis de mas o de menos
          if (parent > 0) {
              throw new Exception("No Cerro " + parent + " parentesis");
          } else if (parent < 0) {
              throw new Exception("Cerro " + parent + " parentesis de mas");
          }
  
          //Al final el resultado de la operacion debe estar en la pila de numeros
          return pilaNum.pop().doubleValue();
      }
  
      //Realiza la operacioin con los ultimos 2 elementos de la pila
     private void operacion(Tipo tipo) {
         //Ultimo numero
         double num2 = pilaNum.pop().doubleValue();
         //Antepenultimo numero
         double num1 = pilaNum.pop().doubleValue();
 
         //los resultados de la operacion se almacenan en la pila
        switch (tipo) {
             case MAS:
                 pilaNum.push(num1 + num2);
                 break;
             case MENOS:
                 pilaNum.push(num1 - num2);
                 break;
             case POR:
                 pilaNum.push(num1 * num2);
                 break;
             case DIVIDE:
                 pilaNum.push(num1 / num2);
                 break;
         }
     }
 
     //Obtiene el siguiente Lexema
     private Lexema siguiente() throws Exception {
         
         if (actual >= expresion.length()) {
             //YA llegamos al final
             return new Lexema(Tipo.FIN);
         } else {
             //Este buffer construye el numero
             StringBuffer numBuff = new StringBuffer();
 
             while (true) {
                 char c = expresion.charAt(actual);
 
                 if (Character.isDigit(c) || c == '.') {
                     //Forma parte de un numero, lo metemos al buffer
                     numBuff.append(c);
                     ++actual;
                 } else {
                     //Se trata de un operador
 
                     if (numBuff.length() > 0) {
                         //Si tiene datos el buffer hay que devolver un Lexema de tipo Numero
                         return new Lexema(Tipo.NUMERO, Double
                                 .parseDouble(numBuff.toString()));
                     } else {
                         Lexema lexema;
 
                         //Devolvemos un Lexema del tipo de la operacion
                         switch (c) {
                             case '+':
                                 lexema = new Lexema(Tipo.MAS);
                                 break;
                             case '-':
                                 lexema = new Lexema(Tipo.MENOS);
                                 break;
                             case '*':
                                 lexema = new Lexema(Tipo.POR);
                                 break;
                             case '/':
                                 lexema = new Lexema(Tipo.DIVIDE);
                                break;
                             case '(':
                                 lexema = new Lexema(Tipo.PAR_IZQ);
                                 break;
                             case ')':
                                 lexema = new Lexema(Tipo.PAR_DER);
                                 break;
                             default:
                                 //El caracter no es un operador
                                 throw new Exception("El operador " + c +
                                         " no es un operador valido");
                         }
 
                         ++actual;
                         return lexema;
                     }
                 }
 
                 if (actual >= expresion.length())  {
                    //Si llegamos al final y hay datos en el buffer hay que
                     //devolver un Lexema numerico
                     return new Lexema(Tipo.NUMERO, Double.parseDouble(
                             numBuff.toString()));
                 }
             }
         }
     }
}