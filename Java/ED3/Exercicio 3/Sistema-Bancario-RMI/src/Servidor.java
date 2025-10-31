import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Implementação do servidor RMI
 * Estende UnicastRemoteObject para fornecer funcionalidade de objeto remoto
 * Implementa a interface InterfaceServidor
 */
public class Servidor extends UnicastRemoteObject implements InterfaceServidor {

    private static final long serialVersionUID = 1L;
    private int totalChamadas;
    private long tempoInicio;

    /**
     * Construtor que lança RemoteException
     */
    public Servidor() throws RemoteException {
        super(); // Chama construtor do UnicastRemoteObject
        this.totalChamadas = 0;
        this.tempoInicio = System.currentTimeMillis();
        System.out.println("[SERVIDOR] Objeto servidor instanciado.");
    }

    /**
     * Implementação do método somar(int a, int b)
     */
    @Override
    public int somar(int a, int b) throws RemoteException {
        totalChamadas++;
        System.out.println("[SERVIDOR] Chamada recebida: somar(" + a + ", " + b + ")");

        int resultado = a + b;

        System.out.println("[SERVIDOR] Resultado calculado: " + resultado);
        return resultado;
    }

    /**
     * Implementação do método somar(int[] vetor)
     */
    @Override
    public int somar(int[] vetor) throws RemoteException {
        totalChamadas++;
        System.out.println("[SERVIDOR] Chamada recebida: somar(vetor)");

        if (vetor == null || vetor.length == 0) {
            System.out.println("[SERVIDOR] Vetor vazio ou nulo retornando 0");
            return 0;
        }

        int soma = 0;
        for (int valor : vetor) {
            soma += valor;
        }

        System.out.println("[SERVIDOR] Soma do vetor: " + soma);
        return soma;
    }

    /**
     * Implementação do método upperCase(String texto)
     */
    @Override
    public String upperCase(String texto) throws RemoteException {
        totalChamadas++;
        System.out.println("[SERVIDOR] Chamada recebida: upperCase(\"" + texto + "\")");

        if (texto == null) {
            return "TEXTO_NULO";
        }

        String resultado = texto.toUpperCase();
        System.out.println("[SERVIDOR] Texto convertido: " + resultado);
        return resultado;
    }

    /**
     * Método adicional para obter status do servidor
     */
    @Override
    public String getStatusServidor() throws RemoteException {
        totalChamadas++;
        long tempoAtividade = System.currentTimeMillis() - tempoInicio;

        String status = "Servidor RMI Ativo - " +
                "Chamadas recebidas: " + totalChamadas + " - " +
                "Tempo de atividade: " + (tempoAtividade / 1000) + " segundos";

        System.out.println("[SERVIDOR] Status solicitado: " + status);
        return status;
    }

    /**
     * Método para obter estatísticas internas (não remoto)
     */
    public String getEstatisticas() {
        return "Total de chamadas: " + totalChamadas;
    }
}