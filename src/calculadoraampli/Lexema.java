/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraampli;

/**
 *
 * @author Alber
 */
public class Lexema {
    
 Tipo tipo;
 
         Double valor;
 
         public Lexema(Tipo tipo) {
             this.tipo = tipo;
         }
 
         public Lexema(Tipo tipo, Double valor) {
             this.tipo = tipo;
             this.valor = valor;
         }
     }
     
     /** Tipos de Lexemas */
     enum Tipo {
         MAS(0), MENOS(0), POR(1), DIVIDE(1), PAR_IZQ(2), PAR_DER(2), NUMERO(-1),
         FIN(-1);
 
         //Establece la prioridad del operador
         int prioridad;
 
         private Tipo(int prioridad) {
             this.prioridad = prioridad;
         }
     }
 
