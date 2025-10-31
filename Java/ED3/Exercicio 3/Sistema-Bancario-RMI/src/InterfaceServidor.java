import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota que define os métodos que podem ser invocados remotamente
 * Estende java.rmi.Remote e todos os métodos devem lançar RemoteException
 */
public interface InterfaceServidor extends Remote {

    /**
     * Soma dois números inteiros
     * @param a primeiro número
     * @param b segundo número
     * @return resultado da soma
     * @throws RemoteException
     */
    int somar(int a, int b) throws RemoteException;

    /**
     * Soma os elementos de um vetor de inteiros
     * @param vetor array de inteiros
     * @return soma dos elementos do vetor
     * @throws RemoteException
     */
    int somar(int[] vetor) throws RemoteException;

    /**
     * Converte uma string para maiúsculas
     * @param texto string a ser convertida
     * @return string em maiúsculas
     * @throws RemoteException
     */
    String upperCase(String texto) throws RemoteException;

    /**
     * Método adicional para demonstrar mais funcionalidades
     * @return mensagem de status do servidor
     * @throws RemoteException
     */
    String getStatusServidor() throws RemoteException;
}