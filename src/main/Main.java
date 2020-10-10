/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Random;
import java.util.Scanner;
import java.time.Duration;

/**
 *
 * @author logra
 */
public class Main {

    /**
     * @param args the command line arguments
     * @return 
     */
    public static Porcion[][] contar_minas(Porcion[][] tablero){
        int mov[][] = {{1,-1,0,0,-1,-1,1,1},{0,0,1,-1,-1,1,-1,1}};
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                for(int k = 0; k < 8; k++){
                    if((i + mov[0][k]) < 0 || (i + mov[0][k]) > 14 || (j + mov[1][k]) < 0 || (j + mov[1][k]) > 14) continue;
                    else if(tablero[i][j].posicion == '*') continue;
                    else{
                        Porcion temp = tablero[i + mov[0][k]][j + mov[1][k]];
                        if(temp.posicion == '*'){
                            tablero[i][j].num_bombas += 1;
                        }
                    }
                }
            }
        }
        return tablero;
    }
    
    public static Porcion[][] generar_tablero(int fila, int columna){
        Porcion[][] tablero = new Porcion[15][15];
        String tipo_mina = "*+++++";
        Random rnd = new Random();
        int cont_minas = 30;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if( i == fila && j == columna){
                    tablero[i][j] = new Porcion();
                    tablero[i][j].visitado = false;
                    tablero[i][j].bandera = false;
                    tablero[i][j].num_bombas =  0;
                    tablero[i][j].posicion = '+';
                    for(int n = fila - 1; n <= fila + 1; n++){
                        for(int m = columna - 1; m <= columna + 1; m++){
                            if((n >= 0 && n <= 14) && (m >= 0 && m <= 14)){
                                if(tablero[n][m] == null){
                                    tablero[n][m] = new Porcion();
                                    tablero[n][m].visitado = false;
                                    tablero[n][m].bandera = false;
                                    tablero[n][m].num_bombas =  0;
                                    tablero[n][m].posicion = '+';
                                }
                                else if(tablero[n][m].posicion == '*'){
                                    tablero[n][m].posicion = '+';
                                    cont_minas++;
                                }
                                
                            }
                        }
                    }
                }else if(tablero[i][j] == null){
                    tablero[i][j] = new Porcion();
                    tablero[i][j].visitado = false;
                    tablero[i][j].bandera = false;
                    tablero[i][j].num_bombas =  0;
                    tablero[i][j].posicion = tipo_mina.charAt(rnd.nextInt(tipo_mina.length()));
                    if(tablero[i][j].posicion == '*' && cont_minas <= 0){
                        tablero[i][j].posicion = '+';
                    }
                    else if(tablero[i][j].posicion == '*'){
                        cont_minas--;
                    }
                }
            }  
        }
        Porcion[][] nuevo = contar_minas(tablero);
        
        return nuevo;
    }
    
    public static void explorar (Porcion[][] tablero, int fila, int columna){
        for(int i = fila - 1; i <= fila + 1; i++){
            for(int j = columna - 1; j <= columna + 1; j++){
                if((i >= 0 && i <= 14) && (j >= 0 && j <= 14)){
                    if(!tablero[i][j].visitado){
                        tablero[i][j].visitado = true;
                        if(tablero[i][j].num_bombas > 0) continue;
                        else if(tablero[i][j].posicion == '*'){
                            tablero[i][j].visitado = false;
                            continue;
                        }
                        else if(tablero[i][j].num_bombas == 0 && tablero[i][j].bandera == false){
                            explorar(tablero, i, j);
                        }
                    }
                }  
            }
        }
    }
    
    public static int num_minas(Porcion[][] tablero){
        int num = 0;
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tablero[i][j].posicion == '*') num++;
            }
        }
        return num;
    }
    
    public static int ganar(Porcion[][] tablero){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tablero[i][j].posicion == '*') continue;
                if(!tablero[i][j].visitado) return 0;
            }
        }
        return 2;
    }
    
    public static int win_condition(Porcion[][] tablero,int fila, int columna){
        if(tablero[fila - 1][columna - 1].posicion  ==  '*'){
            return 1;
        }else if(tablero[fila - 1][columna - 1].num_bombas > 0){
            tablero[fila - 1][columna - 1].visitado = true;
        }else{
            explorar(tablero, fila, columna);
        }
        return 0;
    }
    
    public static void imprimir_tablero(Porcion[][] tablero, int win){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tablero[i][j] == null){
                    System.out.print("# ");
                }
                else if(tablero[i][j].visitado == true){
                    if(tablero[i][j].num_bombas > 0){
                        System.out.print(tablero[i][j].num_bombas + " ");
                    }else if(tablero[i][j].posicion == '*'){
                        System.out.print(tablero[i][j].posicion + " ");
                    }else{
                        System.out.print(tablero[i][j].posicion + " ");
                    }
                    
                }else{
                    if(win == 1){
                        if(tablero[i][j].posicion == '*' || tablero[i][j].visitado == true){
                            System.out.print(tablero[i][j].posicion + " ");
                        }else{
                            System.out.print("# ");
                        }
                    }
                    else if(win == 2){
                        System.out.print(tablero[i][j].posicion + " ");
                    }
                    else if(tablero[i][j].bandera == true){
                        System.out.print("B ");
                    }
                    else{
                        System.out.print("# ");
                    }
                }
            }
            System.out.print("\n");
        }
    }
    
    public static boolean bandera(Porcion[][] tablero,int fila, int columna){
        if(tablero[fila][columna].bandera == false){
            return true;
        }
        return false;
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        Main programa = new Main();
        long inicio = System.nanoTime();
        Porcion[][] tablero = new Porcion[15][15];
        Scanner sc = new Scanner(System.in);
        String opc;
        
        System.out.println("\tBUSCAMINAS");
        System.out.println("  Aviso: para poner bandera debe");
         System.out.println("haber tirado una vez por lo menos");
        System.out.println("\tSimbolos");
        System.out.println(" # = Casilla sin descubrir");
        System.out.println(" * = Mina / bomba");
        System.out.println(" + = Casilla descubierta (sin minas)");
        System.out.println(" 1 = Casilla descubierta (con minas)");
        System.out.println(" B = Casilla con bandera (15 max.)");

        int win_condition = 0;
        int num_tiros = 0;
        int minas = 0;
        boolean tiro1 = true;
        
        while(win_condition == 0){
            System.out.println("\n\tTablero actual\n");
            Main.imprimir_tablero(tablero, win_condition);
            System.out.println("\nB = Bandera | C = Casilla");
            System.out.print("Seleccione una opcion: ");
            opc = sc.next();
            System.out.print("\nNumero de fila [1-15]: ");
            int fila = sc.nextInt();
            System.out.print("Numero de columna [1-15]: ");
            int columna = sc.nextInt();
            
            if(opc.charAt(0) == 'C' || opc.charAt(0) == 'c'){
                if(tiro1 == true){
                    tablero = generar_tablero(fila - 1,columna - 1);
                    minas = num_minas(tablero);
                    tiro1 = false;
                }
                else if(tablero[fila - 1][columna - 1].visitado == true){
                    System.out.println("Posicion ya descubierta.");
                    continue;
                }
                win_condition = win_condition(tablero, fila, columna);
            }else{
                tablero[fila][columna].bandera = bandera(tablero, fila, columna);
            }
            num_tiros++;
            if(ganar(tablero) == 2) win_condition = 2;
        }
        
        if(win_condition == 1){
            System.out.println("\n Lo sentimos. Has perdido =(\n\tTablero final\n");
            Main.imprimir_tablero(tablero, win_condition);
            minas--;
        }else{
            System.out.println("\n Felicidades. Has ganado =)\n\tTablero final\n");
            Main.imprimir_tablero(tablero, win_condition);
        }
        
        long fin = System.nanoTime() - inicio;
        System.out.println("     == Estadisticas ==");
        System.out.println("Tiros: " + num_tiros);
        System.out.println("Minas sin descubrir: " + minas);
        System.out.println("Tiempo: " + fin / 1000000000 + " segundos");
        
        
    }
    
}
