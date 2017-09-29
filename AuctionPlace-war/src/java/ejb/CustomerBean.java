/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entities.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.LoginHelper;

/**
 *
 * @author Daniel Losvik
 */


@Stateless
public class CustomerBean extends AbstractFacade<Customer>{

    @PersistenceContext(unitName="AuctionPlace-warPU")
    private EntityManager em;
    
    public CustomerBean() {
        super(Customer.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public boolean RegisterNewCustomer(Customer customer) throws Exception {

        // check if user exists
        if (getCustomerByEmail(customer.getEmail()) == null) {
            
            // encrypt password with MD5 which is unsecure but fast to implement
            customer.setPassword(LoginHelper.encryptPassword(customer.getPassword()));
            
            super.create(customer);
            return true;
        } else {
            return false;
        }
    }
    
    public Customer loginCustomer(String email, String password) throws Exception {
        Customer customer = getCustomerByEmail(email);
        if(customer != null && customer.getPassword().equals(LoginHelper.encryptPassword(password))) {         
            return customer;
        }
        else {
            return null;
        }
    }

    public Customer getCustomerByEmail(String email) {
        Query createNamedQuery = getEntityManager().createNamedQuery("Customer.findByEmail");

        createNamedQuery.setParameter("email", email);

        if (createNamedQuery.getResultList().size() > 0) {
            return (Customer) createNamedQuery.getSingleResult();
        }
        else {
            return null;
        }
    }
    
    public void editCustomer(Customer customer) {
        this.edit(customer);
    }

}
