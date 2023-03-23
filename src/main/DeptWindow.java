package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exceptions.InstanceNotFoundException;
import modelo.Account;
import modelo.Departamento;
import modelo.servicio.AccountServicio;
import modelo.servicio.DepartamentoServicio;
import modelo.servicio.IAccountServicio;
import modelo.servicio.IDepartamentoServicio;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DeptWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextArea mensajes_text_Area;
	private JList<Account> JListAllAccounts;

	private IAccountServicio cuentasServicio;
	private CreateNewDeptDialog createDialog;
	private JButton btnModificarDepartamento;
	private JButton btnEliminarDepartamento;
	private JLabel lblNewLabel;
	private JTextField JTextFieldEmpno;
	private AccountServicio as;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeptWindow frame = new DeptWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DeptWindow() {

		cuentasServicio = new AccountServicio();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 772);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(8, 8, 821, 714);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 440, 669, 228);
		panel.add(scrollPane);

		mensajes_text_Area = new JTextArea();
		scrollPane.setViewportView(mensajes_text_Area);
		mensajes_text_Area.setEditable(false);
		mensajes_text_Area.setText("Panel de mensajes");
		mensajes_text_Area.setForeground(new Color(255, 0, 0));
		mensajes_text_Area.setFont(new Font("Monospaced", Font.PLAIN, 13));

		JButton btnShowAllCuentas = new JButton("Mostrar cuentas");

		btnShowAllCuentas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnShowAllCuentas.setBounds(50, 67, 208, 36);
		panel.add(btnShowAllCuentas);

		btnModificarDepartamento = new JButton("Modificar importe cuenta");

		JListAllAccounts = new JList<Account>();

		JListAllAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JListAllAccounts.setBounds(403, 37, 377, 200);

		JScrollPane scrollPanel_in_JlistAllDepts = new JScrollPane(JListAllAccounts);
		scrollPanel_in_JlistAllDepts.setLocation(300, 0);
		scrollPanel_in_JlistAllDepts.setSize(500, 250);

		panel.add(scrollPanel_in_JlistAllDepts);

		JButton btnCrearNuevoDepartamento = new JButton("Crear nueva cuenta");

		btnCrearNuevoDepartamento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCrearNuevoDepartamento.setBounds(50, 114, 208, 36);
		panel.add(btnCrearNuevoDepartamento);

		btnModificarDepartamento.setEnabled(false);
		btnModificarDepartamento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnModificarDepartamento.setBounds(50, 161, 208, 36);
		panel.add(btnModificarDepartamento);

		btnEliminarDepartamento = new JButton("Eliminar cuenta");

		btnEliminarDepartamento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEliminarDepartamento.setEnabled(false);
		btnEliminarDepartamento.setBounds(50, 208, 208, 36);
		panel.add(btnEliminarDepartamento);

		lblNewLabel = new JLabel("Introduzca nº de empleado");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(50, 11, 240, 14);
		panel.add(lblNewLabel);

		// Instanciando un objeto AccountServicio a partir de su clase
		as = new AccountServicio();

		JTextFieldEmpno = new JTextField();
		JTextFieldEmpno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (as.doesEmployeeExist(Integer.parseInt(JTextFieldEmpno.getText()))) {
						addMensaje(false, "El empleado existe");
					} else {
						addMensaje(false, "El empleado no existe");
					}
				} catch (InstanceNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// Agregando un KeyListener para capturar la pulsación de Enter
		JTextFieldEmpno.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Disparar el evento de acción cuando se presiona Enter
					JTextFieldEmpno.getActionListeners()[0]
							.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
			}
		});

		JTextFieldEmpno.setBounds(50, 36, 183, 20);
		panel.add(JTextFieldEmpno);
		JTextFieldEmpno.setColumns(10);

		// Eventos
		ActionListener showAllCuentasActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getAllCuentas();
			}
		};
		btnShowAllCuentas.addActionListener(showAllCuentasActionListener);

		ActionListener crearListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFrame owner = (JFrame) SwingUtilities.getRoot((Component) e.getSource());
				createDialog = new CreateNewDeptDialog(owner, "Crear nueva cuenta",
						Dialog.ModalityType.DOCUMENT_MODAL, null);
				showDialog();
			}
		};
		btnCrearNuevoDepartamento.addActionListener(crearListener);

		ActionListener modificarListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIx = JListAllAccounts.getSelectedIndex();
				if (selectedIx > -1) {
					Account cuenta = (Account) JListAllAccounts.getModel().getElementAt(selectedIx);
					if (cuenta != null) {

						JFrame owner = (JFrame) SwingUtilities.getRoot((Component) e.getSource());

						createDialog = new CreateNewDeptDialog(owner, "Modificar departamento",
								Dialog.ModalityType.DOCUMENT_MODAL, cuenta);
						showDialog();
						
					}
				}
			}
		};

		btnModificarDepartamento.addActionListener(modificarListener);

		ListSelectionListener selectionListListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					int selectedIx = JListAllAccounts.getSelectedIndex();
					btnModificarDepartamento.setEnabled((selectedIx > -1));
					btnEliminarDepartamento.setEnabled((selectedIx > -1));
					if (selectedIx > -1) {
						
						Departamento d = (Departamento) DeptWindow.this.JListAllAccounts.getModel()
								.getElementAt(selectedIx);
						if (d != null) {
							addMensaje(true, "Se ha seleccionado el d: " + d);
						}
					}
					
				}
			}
		};
		
		
		JListAllAccounts.addListSelectionListener(selectionListListener);

		ActionListener deleteListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIx = JListAllAccounts.getSelectedIndex();
				if (selectedIx > -1) {
					Departamento d = (Departamento) JListAllAccounts.getModel().getElementAt(selectedIx);
					if (d != null) {
						try {
							boolean exito = departamentoServicio.delete(d.getDeptno());
							if (exito) {
								addMensaje(true, "Se ha eliminado el dept con id: " + d.getDeptno());
								getAllCuentas();
							}
						} catch (exceptions.InstanceNotFoundException e1) {
							addMensaje(true, "No se ha podido borrar el departamento. No se ha encontrado con id: "
									+ d.getDeptno());
						} catch (Exception ex) {
							addMensaje(true, "No se ha podido borrar el departamento. ");
							System.out.println("Exception: " + ex.getMessage());
							ex.printStackTrace();
						}
					}
				}
			}
		};
		btnEliminarDepartamento.addActionListener(deleteListener);
	}


	private void addMensaje(boolean keepText, String msg) {
		String oldText = "";
		if (keepText) {
			oldText = mensajes_text_Area.getText();

		}
		oldText = oldText + "\n" + msg;
		mensajes_text_Area.setText(oldText);

	}

	private void showDialog() {
		createDialog.setVisible(true);
		Departamento departamentoACrear = createDialog.getResult();
		if (departamentoACrear != null) {

			//saveOrUpdate(departamentoACrear);
		}
	}

	
	private void saveOrUpdate(Departamento dept) {
		try {
			Departamento nuevo = departamentoServicio.saveOrUpdate(dept);
			if (nuevo != null) {
				addMensaje(true, "Se ha creado/actualizado un departamento con id: " + nuevo.getDeptno());
				getAllCuentas();
			} else {
				addMensaje(true, " El departamento no se ha creado/actualizado correctamente");
			}

		} catch (Exception ex) {
			addMensaje(true, "Ha ocurrido un error y no se ha podido crear el departamento");
		}
	}
	
	
//después de modificar muchos componentes a la clase correspondiente (de Departamento a Account, casi siempre), modificamos
//la función para que, mediante el método getEmpnoFromTextField(), se recuperen las cuentas mediante un método que ya 
//habíamos creado anteriormente en AccountServicio (y antes, en su interfaz IAccountServicio
	private void getAllCuentas() {
		int numeroEmpleado = getEmpnoFromTextField();
		if(numeroEmpleado != -1) {
			try {
				List<Account> cuentas = cuentasServicio.findAccountByEmployeeId(numeroEmpleado);
				addMensaje(true, "Se han recuperado: " + cuentas.size() + " cuentas");
				DefaultListModel<Account> defModel = new DefaultListModel<>();
				
				defModel.addAll(cuentas);
				
				JListAllAccounts.setModel(defModel);
			}catch(Exception e) {
				addMensaje(true, "Error. No se han podido recuperar cuentas de empleado.");
			}
		}else {
			addMensaje(true, "El número de empleado no es correcto");
		}

	}
//creamos una función que extrae del JTextFieldEmpno su contenido (el núemero de empleado del cual recuperar las cuentas
//inicialmente inicializa la variable que contendrá el Empno a -1; esto es para que, en caso de error, la función devuelva -1
	private int getEmpnoFromTextField() {
		int empno = -1;
		String textIntroducido = JTextFieldEmpno.getText().trim();
		try {
			empno = Integer.parseInt(textIntroducido);
		} catch (Exception nfe) {
			empno = -1;
		}
		return empno;
	}

}
