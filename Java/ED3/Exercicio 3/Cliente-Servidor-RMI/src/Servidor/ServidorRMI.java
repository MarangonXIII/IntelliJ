package Servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServidorRMI extends UnicastRemoteObject implements InterfaceServidorRMI {

    private static final long serialVersionUID = 1L;
    private int totalChamadas;
    private long tempoInicio;

    public ServidorRMI() throws RemoteException {
        super();
        this.totalChamadas = 0;
        this.tempoInicio = System.currentTimeMillis();
        System.out.println("[SERVIDOR] Objeto servidor instanciado.");
    }

    @Override
    public int somar(int a, int b) throws RemoteException {
        totalChamadas++;
        System.out.println("[SERVIDOR] Chamada recebida: somar(" + a + ", " + b + ")");
        int resultado = a + b;
        System.out.println("[SERVIDOR] Resultado calculado: " + resultado);
        return resultado;
    }

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

    @Override
    public String getStatusServidor() throws RemoteException {
        totalChamadas++;
        long tempoAtividade = System.currentTimeMillis() - tempoInicio;
        String status = "Servidor.Servidor RMI Ativo - " + "Chamadas recebidas: " + totalChamadas + " - " + "Tempo de atividade: " + (tempoAtividade / 1000) + " segundos";
        System.out.println("[SERVIDOR] Status solicitado: " + status);
        return status;
    }
    public String getEstatisticas() {
        return "Total de chamadas: " + totalChamadas;
    }
}