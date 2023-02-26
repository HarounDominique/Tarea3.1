package modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import exceptions.SaldoInsuficienteException;
import modelo.AccMovement;
import modelo.Account;
import modelo.Empleado;
import exceptions.InstanceNotFoundException;
import util.SessionFactoryUtil;

public class AccountServicio implements IAccountServicio {

	@Override
	public Account findAccountById(int accId) throws InstanceNotFoundException {
		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Account account = session.get(Account.class, accId);
		if (account == null) {
			throw new InstanceNotFoundException(Account.class.getName());
		}

		session.close();
		return account;
	}



	@Override
	public AccMovement transferir(int accOrigen, int accDestino, double cantidad)
			throws SaldoInsuficienteException, InstanceNotFoundException, UnsupportedOperationException {

		Transaction tx = null;
		Session session = null;
		AccMovement movement = null;

		try {

			if (cantidad <= 0) {
				throw new UnsupportedOperationException();
			}
				SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
				session = sessionFactory.openSession();

				Account accountOrigen = session.get(Account.class, accOrigen);
				if (accountOrigen == null) {
					throw new InstanceNotFoundException(Account.class.getName() + " origen id:" + accOrigen);
				}
				BigDecimal cantidadBD = new BigDecimal(cantidad);
				if (accountOrigen.getAmount().compareTo(cantidadBD) < 0) {
					throw new SaldoInsuficienteException("No hay saldo suficiente", accountOrigen.getAmount(),
							cantidadBD);
				}
				Account accountDestino = session.get(Account.class, accDestino);
				if (accountDestino == null) {
					throw new InstanceNotFoundException(Account.class.getName() + " destino id:" + accDestino);
				}
				
					tx = session.beginTransaction();

					accountOrigen.setAmount(accountOrigen.getAmount().subtract(cantidadBD));
					accountDestino.setAmount(accountDestino.getAmount().add(cantidadBD));

					movement = new AccMovement();
					movement.setAmount(cantidadBD);
					movement.setDatetime(LocalDateTime.now());

					// Relación bidireccional
					movement.setAccountOrigen(accountOrigen);
					movement.setAccountDestino(accountDestino);
					//Son prescindibles y no recomendables en navegación bidireccional porque una Account puede tener numerosos movimientos
//					accountOrigen.getAccMovementsOrigen().add(movement);
//					accountDestino.getAccMovementsDest().add(movement);

//					session.saveOrUpdate(accountOrigen);
//					session.saveOrUpdate(accountDestino);
					session.save(movement);

					tx.commit();
				
			
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una exception: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
		finally {
			if(session!=null) {
				session.close();
			}
		}

		return movement;

	}



	@Override
	public List<Account> findAccountByEmployeeId(int empId) throws InstanceNotFoundException {

	    SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
	    Session session = sessionFactory.openSession();

	    List<Account> consulta = (List<Account>) session.createQuery("SELECT e.accounts FROM Empleado e WHERE e.empno = :empId")
	            .setParameter("empId", empId)
	            .list();

	    session.close();

	    return consulta;
	}
	
	//Método que indica si existe un empleado dado su id o no
	@Override
	public boolean doesEmployeeExist(int empId, JTextArea elemento) throws InstanceNotFoundException {
	    boolean flag = false;
	    SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
	    Session session = sessionFactory.openSession();
	    Empleado consulta = (Empleado) session.createQuery("SELECT e FROM Empleado e WHERE e.empno = :empId")
	            .setParameter("empId", empId)
	            .uniqueResult();
	    if (consulta != null) {
	        flag = true;
	    }
	    if(flag) {
	    	elemento.setText("El empleado existe");
	    }else {
	    	elemento.setText("El empleado no existe");
	    }
	    return flag;
	}

/*
	@Override
	public boolean doesEmployeeExist(int empId) throws InstanceNotFoundException {
		boolean flag = true;
		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
	    Session session = sessionFactory.openSession();
		Empleado consulta = (Empleado) session.createQuery("SELECT e FROM Empleado e WHERE e.empno = :empId")
	            .setParameter("empId", empId)
	            .uniqueResult();
		return flag;
	}

*/
}
