package app.banco.entidades.abstractas;


public abstract class CuentaBancaria {

    protected int id;

    protected String numeroCuenta;

    protected double saldo;

    protected String TipoCuenta;

    protected int idUsuario;

    protected int numeroDepositos = 0;

    protected int numeroRetiros = 0;

    protected int transferenciasACuentaDeAhorro = 0;
    protected int transferenciasACuentaCorriente = 0;

    public CuentaBancaria(String numeroCuenta, String tipoCuenta, int idUsuario) {
        this.numeroCuenta = numeroCuenta;
        TipoCuenta = tipoCuenta;
        this.idUsuario = idUsuario;
    }

    public abstract double depositar(double depositoDinero);

    public abstract double retirar(double retiroDinero);

    public abstract void transferir(String cuentaDestino, double cantidadATransferir);

    public abstract void imprimirCuentaBancaria();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getTipoCuenta() {
        return TipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        TipoCuenta = tipoCuenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getNumeroDepositos() {
        return numeroDepositos;
    }

    public void setNumeroDepositos(int numeroDepositos) {
        this.numeroDepositos = numeroDepositos;
    }

    public int getNumeroRetiros() {
        return numeroRetiros;
    }

    public void setNumeroRetiros(int numeroRetiros) {
        this.numeroRetiros = numeroRetiros;
    }
}