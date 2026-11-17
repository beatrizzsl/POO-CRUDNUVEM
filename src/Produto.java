import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

// Ana Beatriz Soares Lima - 0023872

public class Produto {

    public static void main(String[] args) {
        
        // Conexão na nuvem (Railway)
        String url = "jdbc:mysql://reseau.proxy.rlwy.net:56930/railway";
        String usuariobanco = "root";
        String bancosenha = "OZzGbOYdBalJmrRQKrJdEMVzBAKlYOKF"; 
        
        Scanner sc = new Scanner(System.in);
        
        try {
            Connection conexao = DriverManager.getConnection(url, usuariobanco, bancosenha);
            System.out.println("Conexão com o banco na nuvem estabelecida!");
            
            // --- CRIA A TABELA AUTOMATICAMENTE SE ELA NÃO EXISTIR ---
            String sqlCriarTabela = "CREATE TABLE IF NOT EXISTS PRODUTO ("
                                  + "CODIGO INT PRIMARY KEY, "
                                  + "NOME VARCHAR(100), "
                                  + "PRECO FLOAT)";
            PreparedStatement stmtTabela = conexao.prepareStatement(sqlCriarTabela);
            stmtTabela.execute();
            // --------------------------------------------------------
            
            int opcao = -1;
                    
            while(opcao != 0) {
                System.out.println("\n1 - Inserir | 2 - Listar | 3 - Atualizar | 4 - Deletar | 0 - Sair");
                opcao = sc.nextInt();
                
                switch(opcao) {
                case 1: 
                    System.out.print("Código do produto: ");
                    int codigo = sc.nextInt();
                    
                    System.out.print("Nome do Produto (sem espaços): ");
                    String nome = sc.next();
                    
                    System.out.print("Preço do produto: ");
                    float preco = sc.nextFloat();
                    
                    String sql = "INSERT INTO PRODUTO (CODIGO, NOME, PRECO) VALUES (?, ?, ?)";
                    
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, codigo);
                    stmt.setString(2, nome);
                    stmt.setFloat(3, preco);
                    stmt.executeUpdate();
                    System.out.println("Registro inserido com sucesso!");
                    break;
                    
                case 2:
                    System.out.print("Digite o código do produto para buscar: ");
                    int codigo2 = sc.nextInt();
                    
                    String sql2 = "SELECT * FROM PRODUTO WHERE CODIGO = ?";
                    PreparedStatement stmt2 = conexao.prepareStatement(sql2);
                    
                    stmt2.setInt(1, codigo2);
                    ResultSet rs = stmt2.executeQuery();
                    
                    boolean encontrou = false;
                    while(rs.next()) {
                        encontrou = true;
                        System.out.println("Nome do Produto: " + rs.getString("nome") + 
                                           " | Preço: " + rs.getFloat("preco"));
                    }
                    if(!encontrou) System.out.println("Produto não encontrado.");
                    break;
                    
                case 3: 
                    System.out.print("Digite o código do produto para atualizar: ");
                    int codPesquisa = sc.nextInt();
                    
                    System.out.print("Digite o novo preço do produto: ");
                    float novopreco = sc.nextFloat();
                    
                    String sqlUpd = "UPDATE PRODUTO SET PRECO = ? WHERE CODIGO = ?";
                    
                    PreparedStatement stmtUp = conexao.prepareStatement(sqlUpd);
                    stmtUp.setFloat(1, novopreco);
                    stmtUp.setInt(2, codPesquisa);
                    
                    int linhaAfetada = stmtUp.executeUpdate();
                    
                    if(linhaAfetada > 0) {
                        System.out.println("Preço Atualizado com Sucesso!");
                    } else {
                        System.out.println("Produto não localizado.");
                    }
                    break;
                    
                case 4:
                    System.out.print("Digite o código do produto para deletar: ");
                    int coddelete = sc.nextInt();
                    
                    String sqlDel = "DELETE FROM PRODUTO WHERE CODIGO = ?";
                    
                    PreparedStatement stmtDele = conexao.prepareStatement(sqlDel);
                    stmtDele.setInt(1, coddelete);
                    
                    int linhaAfetada2 = stmtDele.executeUpdate();
                    
                    if(linhaAfetada2 > 0) {
                        System.out.println("Deletado com Sucesso!");
                    } else {
                        System.out.println("Produto não localizado.");
                    }
                    break;
                    
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                    
                default:
                    System.out.println("Opção Inválida!");
                }
            }
            
            sc.close();
            conexao.close();
    
        } catch(Exception e) {
            System.out.println("Erro no Banco de Dados: " + e.getMessage());
        }
    }
}