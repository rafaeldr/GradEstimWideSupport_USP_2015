package docm;


import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections; //Usada para ordenar o ArrayList


public class DOCM {
    
    public static double[][] DOCM(double[][] janela, int dimensaoJanela, char DOCMtype)
    {
        // Janela DEVE ser ímpar
        int linhas = dimensaoJanela;
        int colunas = dimensaoJanela;
        
        int[] qtdPassos = new int[1];
        qtdPassos[0] = 0;
        Stack pilhaCaminho = new Stack();
        
        
        //Iniciar no ponto central da janela (DEVE SER IMPAR)
        int centro = (linhas/2)+1 -1; //-1 para ajuste ao índice base 0
        
        double[][] matrizIntensidadeAcumulada = new double[linhas][colunas];
        
        if(DOCMtype == 'b')  // [DOCMb]
        {
            // Guarda a mínima intensidade acumulada até o ponto
            // Só tem serventia para o algoritmo de percurso
            inicializaMinMatriz(matrizIntensidadeAcumulada,linhas,colunas);
        } else //[DOCMd]
        {
            // Guarda a máxima intensidade acumulada até o ponto
            inicializaMaxMatriz(matrizIntensidadeAcumulada,linhas,colunas);
        }

        //Já preenche intensidade Mínina no ponto central
        matrizIntensidadeAcumulada[centro][centro] = janela[centro][centro];
        
        // Matriz que guarda os DOC do centro até o ponto
        ArrayList[][] matrizDOC = new ArrayList[linhas][colunas];
        inicializaDOCMatriz(matrizDOC,linhas,colunas);
        
        // DOCM é obtido pelos máximos da matriz de DOC (em cada posição)
        double[][] outputDOCM = new double[linhas][colunas];
        

        // INICIO DO PERCURSO
        proximoPonto(centro, centro, janela, linhas, colunas, true, qtdPassos, pilhaCaminho, matrizIntensidadeAcumulada, matrizDOC, DOCMtype);
        
        // Já temos os DOCS entre os pontos
        // Queremos o DOCM a partir do MAX
        calculaDOCM(matrizDOC, outputDOCM, linhas, colunas, DOCMtype);
        
        return outputDOCM;
    }

    // Retorna se está no ponto final ou não
    // Retorno FALSE continua; TRUE PARA
    public static boolean proximoPonto(int x, int y, double[][] matriz, int qtdLinhas, int qtdColunas, boolean imprimePassos, 
            int[] passos, Stack pilhaCaminho, double[][] matrizIntensidadeAcumulada, ArrayList[][] matrizDOC, char DOCMtype)
    {
        passos[0]++;

        // VISITOU
        pilhaCaminho.push(x*10+y);
        
        if(imprimePassos){
            imprimeMatriz(matriz, qtdLinhas, qtdColunas);
        }
        
        
        //Verificação da Intensidade Mínima  [DOCMb]
        // Esse ponto tem menor intensidade entre todos já percorridos?
        if(DOCMtype == 'b')
        {
            double minAnterior = minMatriz(matrizIntensidadeAcumulada, qtdLinhas, qtdColunas);
            double minAtual = minAnterior;
            if(matriz[x][y]<minAnterior)
            {
                matrizIntensidadeAcumulada[x][y] = matriz[x][y];
                minAtual = matriz[x][y];
            }
            
            // Cálculo deste DOC (Centro até aqui; este ponto) POR ESTE CAMINHO
            // Será a intensidade mínima acumulada até este ponto; neste caminho
            matrizDOC[x][y].add(minAtual);
        } else //DOCMd
        {
            double maxAnterior = maxMatriz(matrizIntensidadeAcumulada, qtdLinhas, qtdColunas);
            double maxAtual = maxAnterior;
            if(matriz[x][y]>maxAnterior)
            {
                matrizIntensidadeAcumulada[x][y] = matriz[x][y];
                maxAtual = matriz[x][y];
            }
            
            // Cálculo deste DOC (Centro até aqui; este ponto) POR ESTE CAMINHO
            // Será a intensidade mínima acumulada até este ponto; neste caminho
            matrizDOC[x][y].add(maxAtual); 
        }
        
        // Direita
        // Condições: FRONTEIRA(DA JANELA) && PONTO NÃO VISITADO
        if(y+1<=qtdLinhas-1 && (!pilhaCaminho.contains(x*10+y+1)))
        {
            if(proximoPonto(x, y+1, matriz, qtdLinhas, qtdColunas, imprimePassos, passos, pilhaCaminho, matrizIntensidadeAcumulada, matrizDOC, DOCMtype))
            {
                return true;
            }
            else
            {
                // Remove o último ponto visitado (para que ele possa ser visitado futuramente por um caminho diferente deste)
                // Observe que ele não é o ponto atual.
                pilhaCaminho.pop(); // Mas o ponto em [x][y+1]
            }
        }
        // Baixo
        if(x+1<=qtdColunas-1 && (!pilhaCaminho.contains((x+1)*10+y)))
        {
            if(proximoPonto(x+1, y, matriz, qtdLinhas, qtdColunas, imprimePassos, passos, pilhaCaminho, matrizIntensidadeAcumulada, matrizDOC, DOCMtype))
            {
                return true;
            }
            else
            {
                pilhaCaminho.pop();
            }
        }
        // Esquerda
        if(y-1>=0 && (!pilhaCaminho.contains(x*10+y-1)))
        {
            if(proximoPonto(x, y-1, matriz, qtdLinhas, qtdColunas, imprimePassos, passos, pilhaCaminho, matrizIntensidadeAcumulada, matrizDOC, DOCMtype))
            {
                return true;
            }
            else
            {
                pilhaCaminho.pop();
            }
        }
        // Cima
        if(x-1>=0 && (!pilhaCaminho.contains((x-1)*10+y)))
        {
            if(proximoPonto(x-1, y, matriz, qtdLinhas, qtdColunas, imprimePassos, passos, pilhaCaminho, matrizIntensidadeAcumulada, matrizDOC, DOCMtype))
            {
                return true;
            }
            else
            {
                pilhaCaminho.pop();
            }
        }
        
        // NÃO TEM MAIS PARA ONDE IR
        if(DOCMtype == 'b')
        {
            matrizIntensidadeAcumulada[x][y] = Double.MAX_VALUE; //Ponto descartado neste caminho
        } else // [DOCMd]
        {
            matrizIntensidadeAcumulada[x][y] = Double.MIN_VALUE; //Ponto descartado neste caminho
        }
        return false;
    }
    
    public static void imprimeMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                System.out.print(matriz[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
    
    public static double minMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        double minValue;
        minValue = Double.MAX_VALUE;
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                if(matriz[i][j]<minValue)
                {
                    minValue = matriz[i][j];
                }
            }
        }
        
        return minValue;
    }
    
    public static double maxMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        double maxValue;
        maxValue = Double.MIN_VALUE;
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                if(matriz[i][j]>maxValue)
                {
                    maxValue = matriz[i][j];
                }
            }
        }
        
        return maxValue;
    }
    
    public static void inicializaMinMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                matriz[i][j] = Double.MAX_VALUE;
            }
        }
    }
    
    public static void inicializaMaxMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                matriz[i][j] = Double.MIN_VALUE;
            }
        }
    }
    
    public static void inicializaDOCMatriz(ArrayList[][] matriz, int numLinhas, int numColunas)
    {
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                matriz[i][j] = new ArrayList();
            }
        }
    }
    
    public static void imprimePilha(Stack pilha)
    {
        while(!pilha.empty())
        {
            System.out.println(pilha.pop());
        }
    }
    
    public static void calculaDOCM(ArrayList[][] matrizDOC, double[][] outputDOCM, int numLinhas, int numColunas, char DOCMtype)
    {
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                if(DOCMtype == 'b')
                {
                    outputDOCM[i][j] = (double)Collections.max(matrizDOC[i][j]);
                } else // [DOCMd]
                {
                    outputDOCM[i][j] = (double)Collections.min(matrizDOC[i][j]);
                }
            }
        }
    }
    
    
    public static void preencheMatriz(double[][] matriz, int numLinhas, int numColunas)
    {
        int cont = 0;
        for(int i = 0; i< numLinhas; i++)
        {
            for(int j = 0; j<numColunas;j++)
            {
                matriz[i][j] = cont;
                cont++;
            }
        }
    }
    
}

