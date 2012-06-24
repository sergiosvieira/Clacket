package lib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class Window extends javax.swing.JFrame {
	private JLabel lbPhrase, lbTime, lbText, lbDecTime;
	private JTextField tfPhrase;
	private JSpinner spTime;
	private JButton btAdd, btDel, btEdit, btUp, btDown, btStart, btEnd, btPlay,
			btSave;
	private JList ltPhrases;
	private DefaultListModel model;
	private int selected = -1;
	private ArrayList<Phrase> phrases = new ArrayList<Phrase>();
	private int currentIndex = 0;
	private Timer timer = new Timer();
	private boolean playing = false;

	public Window() {
		setTitle("Clacket 1.0 - sergiosvieira@gmail.com");
		setBounds(100, 100, 640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		setVisible(true);
		tfPhrase.requestFocus();
	}

	private void showText(final int index) {
		if (index < phrases.size() && playing == true) {
			lbText.setText(phrases.get(index).getPhrase());
			new Timer().schedule(new TimerTask() {
				public void run() {
					showText(index + 1);
				}
			}, (long) (phrases.get(index).getTime() * 1000.0));
		} else {
			lbText.setText("");
			btStart.setEnabled(true);
			btEnd.setEnabled(false);			
		}
	}

	private void initialize() {
		lbPhrase = new JLabel("Texto");
		lbTime = new JLabel("Tempo");
		lbText = new JLabel("");
		Font font = lbText.getFont();
		lbText.setFont(new Font(font.getFontName(), font.getStyle(), 20));
		lbDecTime = new JLabel("00:00");
		lbDecTime.setFont(new Font(font.getFontName(), font.getStyle(), 20));
		tfPhrase = new JTextField(10);
		tfPhrase.setPreferredSize(new Dimension(100, 25));
		tfPhrase.setColumns(30);
		SpinnerNumberModel spModel = new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
		spTime = new JSpinner(spModel);
		spTime.setPreferredSize(new Dimension(50, 25));

		btAdd = new JButton("Adicionar");
		model = new DefaultListModel();
		ltPhrases = new JList(model);

		btAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected == -1) {
					if (!tfPhrase.getText().isEmpty()) {
						int pos = ltPhrases.getModel().getSize();
						DecimalFormat df = new DecimalFormat("##.00");
						model.add(
								pos,
								tfPhrase.getText() + ","
										+ df.format(spTime.getValue()));
						tfPhrase.setText("");
						tfPhrase.requestFocus();
						btDel.setEnabled(true);
						btEdit.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null,
								"Preencha os campos corretamente",
								"Clacket 1.0", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					btAdd.setText("Adicionar");
					model.remove(selected);
					DecimalFormat df = new DecimalFormat("##.00");
					model.add(
							selected,
							tfPhrase.getText() + ","
									+ df.format(spTime.getValue()));
					tfPhrase.setText("");
					tfPhrase.requestFocus();
					btDel.setEnabled(true);
					btEdit.setEnabled(true);
					selected = -1;
				}
			}
		});

		btDel = new JButton("Apagar");
		btDel.setEnabled(false);
		btDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ltPhrases.getSelectedValue() != null) {
					model.remove(ltPhrases.getSelectedIndex());
					if (model.getSize() == 0) {
						btDel.setEnabled(false);
						btEdit.setEnabled(false);
					}
				}
			}
		});

		btEdit = new JButton("Editar");
		btEdit.setEnabled(false);
		btEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ltPhrases.getSelectedValue() != null) {
					selected = ltPhrases.getSelectedIndex();
					String item = (String) model.get(selected);
					tfPhrase.setText(item.split(",")[0]);
					spTime.setValue(Float.valueOf(item.split(",")[1]));
					btEdit.setEnabled(false);
					btAdd.setText("Salvar");
					btDel.setEnabled(false);
				}
			}
		});

		btUp = new JButton("Cima");
		btUp.setEnabled(false);
		btDown = new JButton("Baixo");
		btDown.setEnabled(false);
		btStart = new JButton("Iniciar");

		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				playing = true;
				phrases.clear();
				for (int i = 0; i < model.getSize(); i++) {
					String item = (String) model.get(i);
					phrases.add(new Phrase(item.split(",")[0], Float
							.valueOf(item.split(",")[1])));
				}
				if (phrases.size() > 0) {
					showText(0);
				}
				btStart.setEnabled(false);
				btEnd.setEnabled(true);
			}
		});

		btEnd = new JButton("Encerrar");
		btEnd.setEnabled(false);
		btEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				playing = false;
				btEnd.setEnabled(false);
				btAdd.setEnabled(true);
			}
		});
		btPlay = new JButton("Tocar");
		btPlay.setEnabled(false);
		
		btSave = new JButton("Salvar");
		btPlay.setEnabled(false);

		getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(lbPhrase, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 25); // gbc.fill =
												// GridBagConstraints.HORIZONTAL;
		panel.add(tfPhrase, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(lbTime, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(spTime, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btAdd, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		panel.add(btEdit, gbc);

		gbc.gridx = 2;
		gbc.gridy = 3;
		panel.add(btDel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		panel.add(btUp, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		panel.add(btDown, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(ltPhrases, gbc);

		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(ltPhrases, BorderLayout.CENTER);

		JPanel footer = new JPanel();
		footer.setLayout(new BorderLayout(10, 10));
		JPanel topFooter = new JPanel();
		topFooter.setLayout(new GridBagLayout());
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		topFooter.add(btStart, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		topFooter.add(btEnd, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		topFooter.add(btPlay, gbc);

		gbc.gridx = 3;
		gbc.gridy = 1;
		topFooter.add(btSave, gbc);

		/*
		gbc.gridx = 0;
		gbc.gridy = 0;
		topFooter.add(lbText, gbc);

		gbc.gridx = 4;
		gbc.gridy = 0;
		topFooter.add(lbDecTime, gbc);
		*/
		
		footer.add(topFooter, BorderLayout.SOUTH);
		footer.add(lbText, BorderLayout.NORTH);
		getContentPane().add(footer, BorderLayout.SOUTH);
	}
}
