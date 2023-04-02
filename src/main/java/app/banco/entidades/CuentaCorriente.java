package app.banco.entidades;

import app.banco.entidades.abstractas.CuentaBancaria;

public class CuentaCorriente extends CuentaBancaria {

    public CuentaCorriente(String numeroCuenta, String tipoCuenta, int idUsuario) {
        super(numeroCuenta, tipoCuenta, idUsuario);
    }

    @Override
    public double depositar(double depositoDinero) {
        this.saldo += depositoDinero;
        numeroDepositos++;
        return this.saldo;
    }

    @Override
    public double retirar(double retiroDinero) {
        if (retiroDinero <= this.saldo && this.numeroRetiros <= 5) {
            this.saldo -= retiroDinero;
            numeroRetiros++;
        }else {
            throw new IllegalArgumentException("Transacción errada por saldo insuficienteo o limite de retiros superados");
        }
        return this.saldo;
    }

    @Override
    public void transferir(String cuentaDestino, double cantidadATransferir) {
        double cobroAdicional = 0, cantidadRealARestar = cantidadATransferir;
        cobroAdicional += (cantidadATransferir*2)/100;
        cantidadRealARestar += cobroAdicional;

        if(!this.getTipoCuenta().equals(cuentaDestino)) {
            if(this.transferenciasACuentaDeAhorro < 2) {
                if(cantidadRealARestar > this.saldo){
                    throw new IllegalArgumentException("No se puede realziar la operación, porque la cantida a transferir es mayor al saldo de la cuenta");
                } else {
                    this.saldo -= cantidadRealARestar;
                    this.transferenciasACuentaDeAhorro++;
                }
            } else {
                throw new IllegalArgumentException("Solo puedes hacer dos transferias a una cuenta de ahorros");
            }
        } else {
            if(cantidadRealARestar > this.saldo){
                throw new IllegalArgumentException("No se puede realziar la operación, porque la cantida a transferir es mayor al saldo de la cuenta");
            } else {
                this.saldo -= cantidadRealARestar;
                this.transferenciasACuentaCorriente++;
            }
        }
    }

    @Override
    public void imprimirCuentaBancaria(){
        int numeroTransacciones = this.numeroDepositos + this.numeroRetiros;
        System.out.println("CUENTA CORRIENTE" + "\n" +
                "Saldo: " + this.saldo + "\n" +
                "Consignaciones: " + numeroDepositos + "\n" +
                "Retiros: " + numeroRetiros + "\n" +
                "Transacciones realizadas: " + numeroTransacciones);
    }

}