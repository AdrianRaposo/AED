package aed.bancofiel;
 
import java.util.Comparator;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
 

/**
 * Implements the code for the bank application.
 * Implements the client and the "gestor" interfaces.
 */
public class BancoFiel implements ClienteBanco, GestorBanco {
 
    // NOTAD. No se deberia cambiar esta declaracion.
    public IndexedList<Cuenta> cuentas;
 
    // NOTAD. No se deberia cambiar esta constructor.
    public BancoFiel() {
        this.cuentas = new ArrayIndexedList<Cuenta>();
    }
 
    // ----------------------------------------------------------------------
    // Anadir metodos aqui ...
    /*Metodo que recibe un Comparator como parametro y devulve la lista de cuentas
    ordenadas segun el parametro*/
    @Override
    public IndexedList<Cuenta> getCuentasOrdenadas(Comparator<Cuenta> cmp) {
        return insertion(cuentas, cmp);
    }
    /*Metodo que recibe como parametros un dni y un saldo inicial
     para crear una cuenta y llama a otro metodo para insertarla en la lista de cuentas ordenada.
     Devuelve el id asignado a la cuenta.
     */
    @Override
    public String crearCuenta(String dni, int saldoInicial) {
 
        return cuentas.get(insertarCuentaOrdenada(new Cuenta (dni, saldoInicial))).getId();
 
    }
    /*Metodo que recibe un string con el id de una cuenta y la elimina de la lista de cuentas.
    Ademas controla la excepcion de que una cuenta no exista o no este vacia*/
    @Override
    public void borrarCuenta(String id) throws CuentaNoExisteExc, CuentaNoVaciaExc {
        if(devuelveCuenta(id).getSaldo()>0) 
            throw new CuentaNoVaciaExc();
        cuentas.remove(devuelveCuenta(id));
 
    }
    /*Metodo que recibe un id de cuenta y una cantidad 
     e ingresa a dicha cuenta esa cantidad. 
     Devuelve el saldo total de la cuenta.
     */
    @Override
    public int ingresarDinero(String id, int cantidad) throws CuentaNoExisteExc {
        devuelveCuenta(id).ingresar(cantidad);
        return devuelveCuenta(id).getSaldo();
    }
    /*Metodo que recibe un id de cuenta y una cantidad a retirar
      Controla la excepcion de que la cuenta no exista y de que 
      la cuenta no tenga suficiente saldo.
      Devuelve el saldo final de la cuenta.
     */
    @Override
    public int retirarDinero(String id, int cantidad) throws CuentaNoExisteExc, InsuficienteSaldoExc {
        devuelveCuenta(id).retirar(cantidad);
        return  devuelveCuenta(id).getSaldo();
    }
    //Metodo que recibe un id de cuenta y devuelve el saldo asignado a dicha cuenta.
    @Override
    public int consultarSaldo(String id) throws CuentaNoExisteExc {
        return devuelveCuenta(id).getSaldo();
    }
    /*Metodo que recibe el id de dos cuentas, origen y destino, 
     *con una cantidad para realizar una transferecia.
     */
    @Override
    public void hacerTransferencia(String idFrom, String idTo, int cantidad)
            throws CuentaNoExisteExc, InsuficienteSaldoExc {
        devuelveCuenta(idTo); //Comprueba que existe una cuenta de destino con ese id
        retirarDinero(idFrom, cantidad);
        ingresarDinero(idTo, cantidad);
    }
    /*Metodo que recibe un dni y devuelve una lista con los id
     * de las cuentas asociadas a dicho dni.
     */
    @Override
    public IndexedList<String> getIdCuentas(String dni) {
        IndexedList<String> res = new ArrayIndexedList<String>();
        for(int i =0; i<cuentas.size();i++) {
            if(cuentas.get(i).getDNI().equals(dni)) {
                res.add(res.size(),cuentas.get(i).getId());
            }
        }
        return res;
    }
    /*Metodo que recibe un dni y devuelve la suma
     * del saldo de las cuentas asociadas a dicho dni.
     */
    @Override
    public int getSaldoCuentas(String dni) {
        int res = 0;
        for(int i =0; i<cuentas.size();i++) {
            if(cuentas.get(i).getDNI().equals(dni)) {
                res+= cuentas.get(i).getSaldo();
            }
        }
        return res;
    }
 
 

    // ----------------------------------------------------------------------
    // NOTAD. No se deberia cambiar este metodo.
    public String toString() {
        return "banco";
    }
    // METODOS AUXILIARES
 
    /*El metodo buscarCuentas realiza una busqueda binaria sobre una lista de cuentas ordenadas 
    por su id*/
    private int buscarCuenta(String id) {
        boolean encontrado= false;
        int res =-1;
        int min= 0;
        int max = cuentas.size()-1; 
        while(!encontrado && min<=max){
            int medio= (max+min)/2;
            if(cuentas.get(medio).getId().compareTo(id)==0) { //devuelve 0 si id coincde
                encontrado= true;
                res=medio;
            }
            else if(cuentas.get(medio).getId().compareTo(id)<0) { //Devuelve <0 si esta a la izquierda
                max =medio-1;
            }
            else //si >0 entonces solo queda que este a la derecha
                min=medio+1;
 
        }
        return res;
 
    }
    /*Metodo auxiliar que recibe un id y devuelve una cuenta
     * asociada a dicho id.
     * Maneja la excepcion de que no exista ninguna cuenta asociada
     * a dicho id.
     */
    private Cuenta devuelveCuenta(String id) throws CuentaNoExisteExc {
        if(buscarCuenta(id)==-1) {
            throw new CuentaNoExisteExc();
        }
        return cuentas.get(buscarCuenta(id));
    }
    /*Metodo generico que inserta ordenadamente un objeto en la lista recibida,
     * segun el Comparator recibido.
     */
    private static <E> void insertar(E e, IndexedList<E> list, Comparator <E> cmp) {
        boolean insertado=false;
        if(!list.isEmpty()) {
            for(int i = 0; i<list.size()&& !insertado; i++) {
                if(cmp.compare(e, list.get(i))<0) {
                    list.add(i, e);
                    insertado = true;
                }
            }
        }
        if(!insertado)
            list.add(list.size(), e);
    }
    //Metodo generico que ordena una lista segun el Comparator que recibe.
    private static <E> IndexedList<E> insertion(IndexedList<E> list, Comparator <E> cmp){
        IndexedList<E> listaOrdenada = new ArrayIndexedList<>();
        for(int i =0; i< list.size(); i++){
            insertar(list.get(i), listaOrdenada, cmp);
        }
        return listaOrdenada;
    }
    /*Metodo que recibe una cuenta y la anade a la lista de cuentas de forma ordenada segun el id.
    Devuelve la posicion de dicha cuenta en la lista.*/
    private int insertarCuentaOrdenada(Cuenta cuenta) {
        int pos=0;
        boolean insertado=false;
        if(!cuentas.isEmpty()) {
            for(int i = 0; i<cuentas.size()&& !insertado; i++){
                if(compareId(cuentas.get(i),cuenta)<0) {
                    cuentas.add(i, cuenta);
                    insertado = true;
                    pos=i;
                }
            }
        }
        if(!insertado) {
            cuentas.add(cuentas.size(), cuenta);
            pos=cuentas.size()-1;
        }
        return pos;
    }
    //Metodo auxiliar que compara dos cuentas segun su id.
    private int compareId(Cuenta c1, Cuenta c2){  
        return new String(c1.getId()).compareTo(new String(c2.getId()));
    } 
}