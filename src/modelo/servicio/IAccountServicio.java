package modelo.servicio;

import exceptions.SaldoInsuficienteException;
import modelo.AccMovement;
import modelo.Account;

import java.util.List;

import exceptions.InstanceNotFoundException;

public interface IAccountServicio {
	public Account findAccountById(int accId) throws InstanceNotFoundException ;
	
	public List<Account> findAccountByEmployeeId(int empId) throws InstanceNotFoundException ;
	
	public AccMovement transferir(int accOrigen, int accDestino, double cantidad)
			throws SaldoInsuficienteException, InstanceNotFoundException, UnsupportedOperationException ;
		
		
}
