import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculadora extends Remote {
    /**
     * Método remoto para somar dois números inteiros
     * @param a Primeiro número
     * @param b Segundo número  
     * @return Soma dos dois números
     * @throws RemoteException Se ocorrer erro na comunicação remota
     */
    int somar(int a, int b) throws RemoteException;

    /**
     * Método remoto para subtrair dois números inteiros
     * @param a Primeiro número
     * @param b Segundo número
     * @return Subtração dos dois números
     * @throws RemoteException Se ocorrer erro na comunicação remota
     */
    int subtrair(int a, int b) throws RemoteException;

    /**
     * Método remoto para multiplicar dois números inteiros
     * @param a Primeiro número
     * @param b Segundo número
     * @return Multiplicação dos dois números
     * @throws RemoteException Se ocorrer erro na comunicação remota
     */
    int multiplicar(int a, int b) throws RemoteException;

    /**
     * Método remoto para dividir dois números inteiros
     * @param a Numerador
     * @param b Denominador
     * @return Divisão dos dois números
     * @throws RemoteException Se ocorrer erro na comunicação remota
     */
    double dividir(int a, int b) throws RemoteException;
}