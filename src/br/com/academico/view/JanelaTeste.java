package br.com.academico.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;

public class JanelaTeste extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    // ── Dados Pessoais ──────────────────────────────────────────────────────
    private JFormattedTextField txtRgmDP;
    private JTextField          txtNomeDP;
    private JFormattedTextField txtDataNascDP;
    private JFormattedTextField txtCpfDP;
    private JTextField          txtEmailDP;
    private JTextField          txtEndDP;
    private JTextField          txtMunicipioDP;
    private JComboBox<String>   cbUF;
    private JFormattedTextField txtCelularDP;

    // ── Curso ────────────────────────────────────────────────────────────────
    private JComboBox<String> cbCurso;
    private JComboBox<String> cbCampus;
    private JRadioButton      rdbMatutino, rdbVespertino, rdbNoturno;
    private JComboBox<String> cbSemestreAtual;
    private JComboBox<String> cbModalidade;
    private JTextField        txtAnoIngresso;

    // ── Notas e Faltas ───────────────────────────────────────────────────────
    private JTextField        txtRgmNF;
    private JTextField        txtNomeAluno;
    private JTextField        txtCursoAluno;
    private JComboBox<String> cbDisciplina;
    private JComboBox<String> cbSemestre;
    private JComboBox<String> cbNota;
    private JTextField        txtFaltas;

    // ── Boletim ──────────────────────────────────────────────────────────────
    private JFormattedTextField   txtRgmBoletim;
    private JTextField            txtNomeBoletim;
    private JTextField            txtCursoBoletim;
    private JFormattedTextField[] bltDisciplinas = new JFormattedTextField[5];
    private JFormattedTextField[] bltSemestres   = new JFormattedTextField[5];
    private JFormattedTextField[] bltNotas       = new JFormattedTextField[5];
    private JFormattedTextField[] bltFaltas      = new JFormattedTextField[5];
    private JFormattedTextField[] bltSituacoes   = new JFormattedTextField[5];

    // ── Banco de dados em memória ────────────────────────────────────────────
    // RGM -> { Nome, Curso, Campus, Período, DataNasc, CPF, Email, Endereço, Município, UF, Celular, SemestreAtual, Modalidade, AnoIngresso }
    private Map<String, String[]>        bancoDados  = new HashMap<>();
    // RGM -> lista de { Disciplina, Semestre, Nota, Faltas }
    private Map<String, List<String[]>>  notasDados  = new HashMap<>();

    // ════════════════════════════════════════════════════════════════════════
    //  Filtro: somente letras (incluindo acentos portugueses) e espaços
    // ════════════════════════════════════════════════════════════════════════
    static class FiltroSomenteLétras extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr)
                throws BadLocationException {
            if (str != null && str.matches("[\\p{L} ]+"))
                super.insertString(fb, offset, str, attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs)
                throws BadLocationException {
            if (str != null && str.matches("[\\p{L} ]*"))
                super.replace(fb, offset, length, str, attrs);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Filtro: somente dígitos
    // ════════════════════════════════════════════════════════════════════════
    static class FiltroSomenteNumeros extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr)
                throws BadLocationException {
            if (str != null && str.matches("[0-9]*"))
                super.insertString(fb, offset, str, attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs)
                throws BadLocationException {
            if (str != null && str.matches("[0-9]*"))
                super.replace(fb, offset, length, str, attrs);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Dados de exemplo
    // ════════════════════════════════════════════════════════════════════════
    private void popularDados() {
        bancoDados.put("01290202", new String[]{
                "João Silva", "Análise e Desenvolvimento de Sistemas", "Tatuapé", "Noturno",
                "15/03/2000", "123.456.789-00", "joao@email.com", "Rua A, 100",
                "São Paulo", "SP", "(11)99999-0000", "3º", "Presencial", "2022"
        });
        bancoDados.put("01290303", new String[]{
                "Maria Souza", "Engenharia de Software", "Vila Lobos", "Matutino",
                "20/07/2001", "987.654.321-00", "maria@email.com", "Rua B, 200",
                "São Paulo", "SP", "(11)88888-0000", "2º", "Presencial", "2023"
        });
        bancoDados.put("01290404", new String[]{
                "Carlos Lima", "Ciência da Computação", "Tatuapé", "Vespertino",
                "10/11/1999", "111.222.333-44", "carlos@email.com", "Rua C, 300",
                "Guarulhos", "SP", "(11)77777-0000", "4º", "Presencial", "2021"
        });

        List<String[]> notasJoao = new ArrayList<>();
        notasJoao.add(new String[]{"Programação Orientada a Objetos", "2024-1", "8,0",  "2"});
        notasJoao.add(new String[]{"Estrutura de Dados",              "2024-1", "7,0",  "4"});
        notasJoao.add(new String[]{"Banco de Dados",                  "2024-1", "9,0",  "0"});
        notasJoao.add(new String[]{"Cálculo Diferencial 2",           "2024-1", "5,0",  "8"});
        notasDados.put("01290202", notasJoao);

        List<String[]> notasMaria = new ArrayList<>();
        notasMaria.add(new String[]{"Programação Orientada a Objetos", "2024-2", "10,0", "0"});
        notasMaria.add(new String[]{"Estrutura de Dados",              "2024-2", "6,0",  "6"});
        notasDados.put("01290303", notasMaria);

        List<String[]> notasCarlos = new ArrayList<>();
        notasCarlos.add(new String[]{"Programação Orientada a Objetos", "2023-2", "4,0",  "20"});
        notasCarlos.add(new String[]{"Banco de Dados",                  "2023-2", "7,5",  "3"});
        notasCarlos.add(new String[]{"Cálculo Diferencial 2",           "2023-2", "3,5",  "18"});
        notasDados.put("01290404", notasCarlos);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Ações do menu Aluno
    // ════════════════════════════════════════════════════════════════════════
    private void salvarAluno() {
        String rgm = txtRgmDP.getText().replace(" ", "").trim();
        if (rgm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o RGM do aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nome = txtNomeDP.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String periodo = rdbMatutino.isSelected()   ? "Matutino"
                : rdbVespertino.isSelected() ? "Vespertino"
                : rdbNoturno.isSelected()    ? "Noturno"
                : "";

        bancoDados.put(rgm, new String[]{
                nome,
                (String) cbCurso.getSelectedItem(),
                (String) cbCampus.getSelectedItem(),
                periodo,
                txtDataNascDP.getText(),
                txtCpfDP.getText(),
                txtEmailDP.getText(),
                txtEndDP.getText(),
                txtMunicipioDP.getText(),
                (String) cbUF.getSelectedItem(),
                txtCelularDP.getText(),
                (String) cbSemestreAtual.getSelectedItem(),
                (String) cbModalidade.getSelectedItem(),
                txtAnoIngresso.getText()
        });
        JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void alterarAluno() {
        String rgm = txtRgmDP.getText().replace(" ", "").trim();
        if (!bancoDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this,
                    "RGM não encontrado. Use 'Consultar' primeiro para carregar os dados.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        salvarAluno();
        JOptionPane.showMessageDialog(this, "Dados do aluno alterados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void consultarAluno() {
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM para consulta:", "Consultar Aluno", JOptionPane.QUESTION_MESSAGE);
        if (rgm == null) return;
        rgm = rgm.trim();
        if (!bancoDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this, "RGM não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] d = bancoDados.get(rgm);
        txtRgmDP.setText(rgm);
        txtNomeDP.setText(d[0]);
        cbCurso.setSelectedItem(d[1]);
        cbCampus.setSelectedItem(d[2]);
        if      ("Matutino"  .equals(d[3])) rdbMatutino.setSelected(true);
        else if ("Vespertino".equals(d[3])) rdbVespertino.setSelected(true);
        else if ("Noturno"   .equals(d[3])) rdbNoturno.setSelected(true);
        if (d.length > 4)  txtDataNascDP.setText(d[4]);
        if (d.length > 5)  txtCpfDP.setText(d[5]);
        if (d.length > 6)  txtEmailDP.setText(d[6]);
        if (d.length > 7)  txtEndDP.setText(d[7]);
        if (d.length > 8)  txtMunicipioDP.setText(d[8]);
        if (d.length > 9)  cbUF.setSelectedItem(d[9]);
        if (d.length > 10) txtCelularDP.setText(d[10]);
        if (d.length > 11) cbSemestreAtual.setSelectedItem(d[11]);
        if (d.length > 12) cbModalidade.setSelectedItem(d[12]);
        if (d.length > 13) txtAnoIngresso.setText(d[13]);
        tabbedPane.setSelectedIndex(0);
    }

    private void excluirAluno() {
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM para excluir:", "Excluir Aluno", JOptionPane.QUESTION_MESSAGE);
        if (rgm == null) return;
        rgm = rgm.trim();
        if (!bancoDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this, "RGM não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o aluno com RGM " + rgm + "?\nEsta ação também excluirá suas notas.",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            bancoDados.remove(rgm);
            notasDados.remove(rgm);
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Ações do menu Notas e Faltas
    // ════════════════════════════════════════════════════════════════════════
    private void salvarNota() {
        String rgm = txtRgmNF.getText().trim();
        if (!bancoDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this,
                    "RGM não encontrado. Cadastre o aluno primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disciplina = (String) cbDisciplina.getSelectedItem();
        String semestre   = (String) cbSemestre.getSelectedItem();
        String nota       = (String) cbNota.getSelectedItem();
        String faltas     = txtFaltas.getText().trim();

        List<String[]> lista = notasDados.computeIfAbsent(rgm, k -> new ArrayList<>());
        boolean found = false;
        for (String[] e : lista) {
            if (e[0].equals(disciplina) && e[1].equals(semestre)) {
                e[2] = nota; e[3] = faltas; found = true; break;
            }
        }
        if (!found) lista.add(new String[]{disciplina, semestre, nota, faltas});

        JOptionPane.showMessageDialog(this, "Nota salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void alterarNota() {
        String rgm = txtRgmNF.getText().trim();
        if (!notasDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma nota encontrada para este RGM.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        salvarNota();
    }

    private void consultarNota() {
        String rgm = JOptionPane.showInputDialog(this,
                "Digite o RGM para consultar notas:", "Consultar Notas", JOptionPane.QUESTION_MESSAGE);
        if (rgm == null) return;
        rgm = rgm.trim();
        if (!notasDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma nota encontrada para este RGM.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtRgmNF.setText(rgm);
        buscarAluno();
        List<String[]> notas = notasDados.get(rgm);
        StringBuilder sb = new StringBuilder(
                "<html><table border='1' cellpadding='4'>" +
                        "<tr><th>Disciplina</th><th>Semestre</th><th>Nota</th><th>Faltas</th></tr>");
        for (String[] n : notas)
            sb.append("<tr><td>").append(n[0]).append("</td><td>").append(n[1])
                    .append("</td><td>").append(n[2]).append("</td><td>").append(n[3]).append("</td></tr>");
        sb.append("</table></html>");
        JOptionPane.showMessageDialog(this, sb.toString(), "Notas - RGM: " + rgm, JOptionPane.INFORMATION_MESSAGE);
        tabbedPane.setSelectedIndex(2);
    }

    private void excluirNota() {
        String rgm = JOptionPane.showInputDialog(this,
                "Digite o RGM para excluir notas:", "Excluir Notas", JOptionPane.QUESTION_MESSAGE);
        if (rgm == null) return;
        rgm = rgm.trim();
        if (!notasDados.containsKey(rgm)) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma nota encontrada para este RGM.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Excluir todas as notas do RGM " + rgm + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            notasDados.remove(rgm);
            JOptionPane.showMessageDialog(this, "Notas excluídas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Busca – Notas e Faltas
    // ════════════════════════════════════════════════════════════════════════
    private void buscarAluno() {
        String rgm = txtRgmNF.getText().trim();
        if (bancoDados.containsKey(rgm)) {
            String[] d = bancoDados.get(rgm);
            txtNomeAluno.setText(d[0]);
            txtCursoAluno.setText(d[1]);
            txtNomeAluno.setForeground(Color.BLACK);
            txtCursoAluno.setForeground(Color.BLACK);
        } else {
            txtNomeAluno.setText("RGM não encontrado");
            txtCursoAluno.setText("");
            txtNomeAluno.setForeground(Color.RED);
            txtCursoAluno.setForeground(Color.RED);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Busca – Boletim
    // ════════════════════════════════════════════════════════════════════════
    private void buscarBoletim() {
        // Limpa todas as células
        txtNomeBoletim.setText("");
        txtNomeBoletim.setForeground(Color.BLACK);
        txtCursoBoletim.setText("");
        txtCursoBoletim.setForeground(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            bltDisciplinas[i].setText("");
            bltSemestres[i].setText("");
            bltNotas[i].setText("");
            bltFaltas[i].setText("");
            bltSituacoes[i].setText("");
            bltSituacoes[i].setForeground(Color.BLACK);
        }

        String rgm = txtRgmBoletim.getText().trim();
        if (rgm.isEmpty()) return;

        if (!bancoDados.containsKey(rgm)) {
            txtNomeBoletim.setText("RGM não encontrado");
            txtNomeBoletim.setForeground(Color.RED);
            txtCursoBoletim.setText("");
            return;
        }

        String[] d = bancoDados.get(rgm);
        txtNomeBoletim.setText(d[0]);
        txtCursoBoletim.setText(d[1]);

        List<String[]> notas = notasDados.getOrDefault(rgm, new ArrayList<>());
        for (int i = 0; i < notas.size() && i < 5; i++) {
            String[] n = notas.get(i);
            bltDisciplinas[i].setText(n[0]);
            bltSemestres[i].setText(n[1]);
            bltNotas[i].setText(n[2]);
            bltFaltas[i].setText(n[3]);

            double notaVal  = Double.parseDouble(n[2].replace(",", "."));
            int    faltasVal = 0;
            try { faltasVal = Integer.parseInt(n[3].trim()); } catch (NumberFormatException ignored) {}

            if (notaVal >= 5.0 && faltasVal <= 15) {
                bltSituacoes[i].setText("Aprovado");
                bltSituacoes[i].setForeground(new Color(0, 140, 0));
            } else {
                bltSituacoes[i].setText("Reprovado");
                bltSituacoes[i].setForeground(Color.RED);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  main
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JanelaTeste frame = new JanelaTeste();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Construtor
    // ════════════════════════════════════════════════════════════════════════
    public JanelaTeste() throws Exception {

        popularDados();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 648, 438);

        // ── Menu Bar ────────────────────────────────────────────────────────
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu Aluno
        JMenu mnNewMenu = new JMenu("Aluno");
        menuBar.add(mnNewMenu);

        JMenuItem mntmSalvarAluno = new JMenuItem("Salvar");
        mntmSalvarAluno.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        mntmSalvarAluno.addActionListener(e -> salvarAluno());
        mnNewMenu.add(mntmSalvarAluno);

        JMenuItem mntmAlterarAluno = new JMenuItem("Alterar");
        mntmAlterarAluno.addActionListener(e -> alterarAluno());
        mnNewMenu.add(mntmAlterarAluno);

        JMenuItem mntmConsultarAluno = new JMenuItem("Consultar");
        mntmConsultarAluno.addActionListener(e -> consultarAluno());
        mnNewMenu.add(mntmConsultarAluno);

        JMenuItem mntmExcluirAluno = new JMenuItem("Excluir");
        mntmExcluirAluno.addActionListener(e -> excluirAluno());
        mnNewMenu.add(mntmExcluirAluno);

        mnNewMenu.add(new JSeparator());

        JMenuItem mntmSair = new JMenuItem("Sair");
        mntmSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK));
        mntmSair.addActionListener(e -> System.exit(0));
        mnNewMenu.add(mntmSair);

        // Menu Notas e Faltas
        JMenu mnNotas = new JMenu("Notas e Faltas");
        menuBar.add(mnNotas);

        JMenuItem mntmSalvarNota = new JMenuItem("Salvar");
        mntmSalvarNota.addActionListener(e -> salvarNota());
        mnNotas.add(mntmSalvarNota);

        JMenuItem mntmAlterarNota = new JMenuItem("Alterar");
        mntmAlterarNota.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        mntmAlterarNota.addActionListener(e -> alterarNota());
        mnNotas.add(mntmAlterarNota);

        JMenuItem mntmConsultarNota = new JMenuItem("Consultar");
        mntmConsultarNota.addActionListener(e -> consultarNota());
        mnNotas.add(mntmConsultarNota);

        mnNotas.add(new JSeparator());

        JMenuItem mntmExcluirNota = new JMenuItem("Excluir");
        mntmExcluirNota.addActionListener(e -> excluirNota());
        mnNotas.add(mntmExcluirNota);

        // Menu Ajuda (mantido como estava)
        JMenu mnAjuda = new JMenu("Ajuda");
        menuBar.add(mnAjuda);
        JMenuItem menuItem = new JMenuItem("Sobre");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "<html>" +
                                "<h2 style='text-align:center'>Sistema Acadêmico</h2>" +
                                "<b>Desenvolvido por:</b><br>" +
                                "• Agatha Ribeiro<br>" +
                                "• Bruno Oliveira Theodoro<br>" +
                                "• Dandhara Fernandes De Campos Lima<br>" +
                                "• Ellen Mayumi Borges<br>" +
                                "• Felipe Neres Vieira<br>" +
                                "• Guilherme dos Santos Matos<br>" +
                                "• Pedro Fidelis Mandoti<br>" +
                                "• Victor Leandro da Silva<br>" +
                                "• Iris Pfister Pascoal<br><br>" +
                                "<i>Campus: Tatuapé</i>" +
                                "</html>",
                        "Sobre o Sistema",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        mnAjuda.add(menuItem);

        // ── Content Pane ─────────────────────────────────────────────────────
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(20, 21, 604, 346);
        contentPane.add(tabbedPane);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Dados Pessoais", null, panel, null);
        panel.setLayout(null);

        JLabel lblRgm = new JLabel("RGM");
        lblRgm.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblRgm.setBounds(20, 20, 56, 22);
        panel.add(lblRgm);

        txtRgmDP = new JFormattedTextField(new MaskFormatter("#########"));
        txtRgmDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtRgmDP.setBounds(71, 21, 121, 25);
        txtRgmDP.setColumns(10);
        panel.add(txtRgmDP);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNome.setBounds(213, 20, 56, 22);
        panel.add(lblNome);

        txtNomeDP = new JTextField();
        txtNomeDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtNomeDP.setBounds(279, 20, 291, 25);
        txtNomeDP.setColumns(10);
        // Somente letras no campo Nome
        ((AbstractDocument) txtNomeDP.getDocument()).setDocumentFilter(new FiltroSomenteLétras());
        panel.add(txtNomeDP);

        JLabel lblDataNasc = new JLabel("Data de Nascimento");
        lblDataNasc.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblDataNasc.setBounds(20, 75, 172, 22);
        panel.add(lblDataNasc);

        txtDataNascDP = new JFormattedTextField(new MaskFormatter("##/##/####"));
        txtDataNascDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtDataNascDP.setBounds(197, 75, 111, 23);
        panel.add(txtDataNascDP);

        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCpf.setBounds(318, 75, 39, 22);
        panel.add(lblCpf);

        txtCpfDP = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
        txtCpfDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtCpfDP.setBounds(366, 75, 204, 23);
        panel.add(txtCpfDP);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblEmail.setBounds(20, 134, 70, 22);
        panel.add(lblEmail);

        txtEmailDP = new JTextField();
        txtEmailDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtEmailDP.setColumns(10);
        txtEmailDP.setBounds(83, 134, 487, 31);
        panel.add(txtEmailDP);

        JLabel lblEnd = new JLabel("End.");
        lblEnd.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblEnd.setBounds(20, 198, 85, 22);
        panel.add(lblEnd);

        txtEndDP = new JTextField();
        txtEndDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtEndDP.setColumns(10);
        txtEndDP.setBounds(83, 197, 487, 31);
        panel.add(txtEndDP);

        JLabel lblMunicipio = new JLabel("Município");
        lblMunicipio.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblMunicipio.setBounds(20, 258, 85, 22);
        panel.add(lblMunicipio);

        txtMunicipioDP = new JTextField();
        txtMunicipioDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtMunicipioDP.setColumns(10);
        txtMunicipioDP.setBounds(115, 254, 110, 26);

        ((AbstractDocument) txtMunicipioDP.getDocument()).setDocumentFilter(new FiltroSomenteLétras());
        panel.add(txtMunicipioDP);

        JLabel lblUf = new JLabel("UF");
        lblUf.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblUf.setBounds(235, 254, 56, 22);
        panel.add(lblUf);

        cbUF = new JComboBox<>();
        cbUF.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cbUF.setModel(new DefaultComboBoxModel<>(new String[]{
                "AC","AP","AM","PA","RO","RR","TO","AL","BA","CE","MA",
                "PB","PE","PI","RN","SE","DF","GO","MT","MS","ES","MG",
                "RJ","SP","PR","RS","SC"
        }));
        cbUF.setBounds(266, 256, 56, 22);
        panel.add(cbUF);

        JLabel lblCelular = new JLabel("Celular");
        lblCelular.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCelular.setBounds(335, 258, 64, 22);
        panel.add(lblCelular);

        txtCelularDP = new JFormattedTextField(new MaskFormatter("(##)#####-####"));
        txtCelularDP.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtCelularDP.setBounds(409, 258, 161, 23);
        panel.add(txtCelularDP);


        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Curso", null, panel_1, null);
        panel_1.setLayout(null);

        JLabel lblCurso = new JLabel("Curso");
        lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblCurso.setBounds(40, 34, 56, 26);
        panel_1.add(lblCurso);

        cbCurso = new JComboBox<>();
        cbCurso.setFont(new Font("Tahoma", Font.PLAIN, 18));
        cbCurso.setModel(new DefaultComboBoxModel<>(new String[]{
                "Análise e Desenvolvimento de Sistemas",
                "Engenharia de Software",
                "Ciência da Computação"
        }));
        cbCurso.setBounds(126, 34, 433, 27);
        panel_1.add(cbCurso);

        JLabel lblCampus = new JLabel("Campus");
        lblCampus.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblCampus.setBounds(40, 90, 82, 26);
        panel_1.add(lblCampus);

        cbCampus = new JComboBox<>();
        cbCampus.setFont(new Font("Tahoma", Font.PLAIN, 18));
        cbCampus.setModel(new DefaultComboBoxModel<>(new String[]{"Tatuapé", "Vila Lobos"}));
        cbCampus.setBounds(126, 91, 433, 27);
        panel_1.add(cbCampus);

        JLabel lblPeriodo = new JLabel("Período");
        lblPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPeriodo.setBounds(40, 157, 82, 26);
        panel_1.add(lblPeriodo);

        rdbMatutino   = new JRadioButton("Matutino");
        rdbVespertino = new JRadioButton("Vespertino");
        rdbNoturno    = new JRadioButton("Noturno");
        rdbMatutino.setFont(new Font("Tahoma", Font.PLAIN, 18));
        rdbVespertino.setFont(new Font("Tahoma", Font.PLAIN, 18));
        rdbNoturno.setFont(new Font("Tahoma", Font.PLAIN, 18));
        rdbMatutino.setBounds(129, 164, 102, 20);
        rdbVespertino.setBounds(262, 161, 122, 20);
        rdbNoturno.setBounds(420, 161, 102, 20);
        panel_1.add(rdbMatutino);
        panel_1.add(rdbVespertino);
        panel_1.add(rdbNoturno);

        ButtonGroup grupoPeriodo = new ButtonGroup();
        grupoPeriodo.add(rdbMatutino);
        grupoPeriodo.add(rdbVespertino);
        grupoPeriodo.add(rdbNoturno);

        // ── Campos abaixo do Período ────────────────────────────────────────

        JSeparator sepCurso = new JSeparator();
        sepCurso.setBounds(40, 200, 519, 2);
        panel_1.add(sepCurso);

        // Semestre Atual
        JLabel lblSemestreAtual = new JLabel("Semestre Atual");
        lblSemestreAtual.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblSemestreAtual.setBounds(40, 214, 135, 26);
        panel_1.add(lblSemestreAtual);

        cbSemestreAtual = new JComboBox<>();
        cbSemestreAtual.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cbSemestreAtual.setModel(new DefaultComboBoxModel<>(new String[]{"1º","2º","3º","4º","5º","6º"}));
        cbSemestreAtual.setBounds(190, 216, 80, 24);
        panel_1.add(cbSemestreAtual);

        // Modalidade
        JLabel lblModalidade = new JLabel("Modalidade");
        lblModalidade.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblModalidade.setBounds(300, 214, 105, 26);
        panel_1.add(lblModalidade);

        cbModalidade = new JComboBox<>();
        cbModalidade.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cbModalidade.setModel(new DefaultComboBoxModel<>(new String[]{"Presencial", "EAD", "Híbrido"}));
        cbModalidade.setBounds(415, 216, 144, 24);
        panel_1.add(cbModalidade);

        // Ano de Ingresso
        JLabel lblAnoIngresso = new JLabel("Ano de Ingresso");
        lblAnoIngresso.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblAnoIngresso.setBounds(40, 260, 148, 26);
        panel_1.add(lblAnoIngresso);

        txtAnoIngresso = new JTextField();
        txtAnoIngresso.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtAnoIngresso.setBounds(200, 262, 80, 24);
        // Somente números no ano de ingresso
        ((AbstractDocument) txtAnoIngresso.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        panel_1.add(txtAnoIngresso);

        // Turno de aulas (informativo)
        JLabel lblTurnoInfo = new JLabel("Turno de aulas:");
        lblTurnoInfo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTurnoInfo.setForeground(new Color(80, 80, 80));
        lblTurnoInfo.setBounds(40, 300, 130, 18);
        panel_1.add(lblTurnoInfo);

        JLabel lblTurnoDetalhe = new JLabel("Matutino: 07h-12h  |  Vespertino: 13h-18h  |  Noturno: 19h-23h");
        lblTurnoDetalhe.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblTurnoDetalhe.setForeground(new Color(100, 100, 100));
        lblTurnoDetalhe.setBounds(40, 318, 530, 18);
        panel_1.add(lblTurnoDetalhe);


        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("Notas e Faltas", null, panel_2, null);
        panel_2.setLayout(null);

        JLabel lblRgmNF = new JLabel("RGM");
        lblRgmNF.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblRgmNF.setBounds(10, 15, 45, 22);
        panel_2.add(lblRgmNF);

        txtRgmNF = new JFormattedTextField(new MaskFormatter("#########"));
        txtRgmNF.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtRgmNF.setBounds(71, 21, 121, 25);
        txtRgmNF.setColumns(10);
        panel_2.add(txtRgmNF);


        txtRgmNF.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { buscarAluno(); }
        });
        txtRgmNF.addActionListener(e -> buscarAluno());

        // Campo nome (read-only) – aparece à direita do RGM
        txtNomeAluno = new JTextField("Nome");
        txtNomeAluno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtNomeAluno.setEditable(false);
        txtNomeAluno.setForeground(Color.GRAY);
        txtNomeAluno.setBounds(232, 17, 339, 25);
        panel_2.add(txtNomeAluno);

        // Campo curso (read-only) – aparece abaixo
        txtCursoAluno = new JTextField("Curso");
        txtCursoAluno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtCursoAluno.setEditable(false);
        txtCursoAluno.setForeground(Color.GRAY);
        txtCursoAluno.setBounds(10, 61, 560, 25);
        panel_2.add(txtCursoAluno);

        JLabel lblDisciplina = new JLabel("Disciplina");
        lblDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblDisciplina.setBounds(10, 122, 84, 22);
        panel_2.add(lblDisciplina);

        cbDisciplina = new JComboBox<>();
        cbDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 18));
        cbDisciplina.setModel(new DefaultComboBoxModel<>(new String[]{
                "Programação Orientada a Objetos", "Estrutura de Dados",
                "Banco de Dados", "Cálculo Diferencial 2"
        }));
        cbDisciplina.setBounds(91, 121, 480, 25);
        panel_2.add(cbDisciplina);

        JLabel lblSemestre = new JLabel("Semestre");
        lblSemestre.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblSemestre.setBounds(15, 176, 79, 22);
        panel_2.add(lblSemestre);

        cbSemestre = new JComboBox<>();
        cbSemestre.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbSemestre.setModel(new DefaultComboBoxModel<>(new String[]{
                "2020-1","2020-2","2021-1","2021-2","2022-1","2022-2",
                "2023-1","2023-2","2024-1","2024-2","2025-1","2025-2","2026-1","2026-2"
        }));
        cbSemestre.setBounds(111, 176, 90, 25);
        panel_2.add(cbSemestre);

        JLabel lblNota = new JLabel("Nota");
        lblNota.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNota.setBounds(221, 176, 40, 22);
        panel_2.add(lblNota);

        cbNota = new JComboBox<>();
        cbNota.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbNota.setModel(new DefaultComboBoxModel<>(new String[]{
                "0,0","0,5","1,0","1,5","2,0","2,5","3,0","3,5",
                "4,0","4,5","5,0","6,0","7,0","8,0","9,0","10,0"
        }));
        cbNota.setBounds(279, 176, 70, 25);
        panel_2.add(cbNota);

        JLabel lblFaltas = new JLabel("Faltas");
        lblFaltas.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblFaltas.setBounds(380, 176, 70, 22);
        panel_2.add(lblFaltas);

        txtFaltas = new JTextField();
        txtFaltas.setBounds(443, 178, 60, 25);
        panel_2.add(txtFaltas);

        //Boletim
        JPanel panel_3 = new JPanel();
        tabbedPane.addTab("Boletim", null, panel_3, null);
        panel_3.setLayout(null);

        JLabel lblRgmBoletim = new JLabel("RGM");
        lblRgmBoletim.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblRgmBoletim.setBounds(10, 10, 56, 22);
        panel_3.add(lblRgmBoletim);

        txtRgmBoletim = new JFormattedTextField(new MaskFormatter("#########"));
        txtRgmBoletim.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtRgmBoletim.setBounds(76, 15, 162, 18);
        panel_3.add(txtRgmBoletim);
        txtRgmBoletim.addActionListener(e -> buscarBoletim());
        txtRgmBoletim.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { buscarBoletim(); }
        });

        JLabel lblNomeBoletim = new JLabel("Nome");
        lblNomeBoletim.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNomeBoletim.setBounds(262, 10, 56, 22);
        panel_3.add(lblNomeBoletim);

        txtNomeBoletim = new JTextField();
        txtNomeBoletim.setEditable(false);
        txtNomeBoletim.setBounds(328, 15, 251, 18);
        panel_3.add(txtNomeBoletim);

        // Curso – preenchido automaticamente ao digitar o RGM
        JLabel lblCursoBoletim = new JLabel("Curso");
        lblCursoBoletim.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCursoBoletim.setBounds(10, 40, 56, 22);
        panel_3.add(lblCursoBoletim);

        txtCursoBoletim = new JTextField();
        txtCursoBoletim.setEditable(false);
        txtCursoBoletim.setBounds(76, 42, 503, 18);
        panel_3.add(txtCursoBoletim);


        JLabel lblNewLabel_1 = new JLabel("Disciplina");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel_1.setBounds(10, 68, 99, 22);
        panel_3.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Semestre");
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1.setBounds(276, 73, 62, 12);
        panel_3.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel("Notas");
        lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1.setBounds(380, 73, 44, 12);
        panel_3.add(lblNewLabel_1_1_1);

        JLabel lblNewLabel_1_1_1_1 = new JLabel("Faltas");
        lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1_1.setBounds(460, 73, 56, 12);
        panel_3.add(lblNewLabel_1_1_1_1);

        JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Situação");
        lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1_1_1.setBounds(527, 65, 72, 31);
        panel_3.add(lblNewLabel_1_1_1_1_1);




        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(276, 76, 0, 233);
        panel_3.add(separator_1);

        JSeparator separator_1_1 = new JSeparator();
        separator_1_1.setBounds(354, 76, 0, 233);
        panel_3.add(separator_1_1);

        JSeparator separator_1_2 = new JSeparator();
        separator_1_2.setBounds(437, 76, 0, 243);
        panel_3.add(separator_1_2);

        JSeparator separator_1_3 = new JSeparator();
        separator_1_3.setBounds(512, 76, 0, 233);
        panel_3.add(separator_1_3);


        int[] yDisc = { 88, 126, 178, 225, 275 };
        int[] yCols = { 98, 138, 182, 231, 280 };
        int[] ySep  = {122, 166, 219, 263         };

        for (int i = 0; i < 5; i++) {
            // Disciplina
            bltDisciplinas[i] = new JFormattedTextField();
            bltDisciplinas[i].setEditable(false);
            bltDisciplinas[i].setBounds(10, yDisc[i], 256, 28);
            panel_3.add(bltDisciplinas[i]);

            // Semestre
            bltSemestres[i] = new JFormattedTextField();
            bltSemestres[i].setEditable(false);
            bltSemestres[i].setBounds(274, yCols[i], 80, 18);
            panel_3.add(bltSemestres[i]);

            // Nota
            bltNotas[i] = new JFormattedTextField();
            bltNotas[i].setEditable(false);
            bltNotas[i].setBounds(375, yCols[i], 62, 18);
            panel_3.add(bltNotas[i]);

            // Faltas
            bltFaltas[i] = new JFormattedTextField();
            bltFaltas[i].setEditable(false);
            bltFaltas[i].setBounds(451, yCols[i], 62, 18);
            panel_3.add(bltFaltas[i]);

            // Situação
            bltSituacoes[i] = new JFormattedTextField();
            bltSituacoes[i].setEditable(false);
            bltSituacoes[i].setBounds(527, yCols[i], 62, 18);
            panel_3.add(bltSituacoes[i]);

            // Separador horizontal entre linhas (exceto após a última)
            if (i < ySep.length) {
                JSeparator sepRow = new JSeparator();
                sepRow.setBounds(10, ySep[i], 579, 2);
                panel_3.add(sepRow);
            }
        }
    }
}