package app.banco.entidades;

import app.banco.entidades.abstractas.CuentaBancaria;

public class CuentaAhorro extends CuentaBancaria {

    public CuentaAhorro(String numeroCuenta, String tipoCuenta, int idUsuario) {
        super(numeroCuenta, tipoCuenta, idUsuario);
    }

    @Override
    public double depositar(double depositoDinero) {
        if (numeroDepositos <= 2) {
            this.saldo += depositoDinero + (depositoDinero*0.5)/100;
        }else {
            this.saldo += depositoDinero;
        }
        numeroDepositos++;
        return this.saldo;
    }


    @Override
    public double retirar(double retiroDinero) {
        if (retiroDinero < this.saldo) {
            if (this.numeroRetiros > 3) {
                double retiroMasComision = (retiroDinero + (retiroDinero*1)/100);
                if(retiroMasComision < this.saldo){
                    this.saldo -= retiroMasComision;
                } else {
                    throw new IllegalArgumentException("Transacción errada, saldo insuficiente");
                }
            }else {
                this.saldo -= retiroDinero;
            }
            this.numeroRetiros++;
        }else {
            throw new IllegalArgumentException("Transacción errada, saldo insuficiente");
        }
        return this.saldo;
    }

    @Override
    public void transferir(String cuentaDestino, double cantidadATransferir) {
        double cobroAdicional = 0, cantidadRealARestar = cantidadATransferir;

        if(!this.getTipoCuenta().equals(cuentaDestino)){
            cobroAdicional += (cantidadATransferir*1.5)/100;
            cantidadATransferir += cobroAdicional;

            transferenciasACuentaCorriente++;
        } else {
            transferenciasACuentaDeAhorro++;
        }
        if(cantidadRealARestar > this.getSaldo()){
            throw new IllegalArgumentException("No se puede realziar la operación, porque la cantida a transferir es mayor al saldo de la cuenta");
        } else {
            this.saldo -= cantidadATransferir;
        }
    }

    @Override
    public void imprimirCuentaBancaria(){
        int numeroTransacciones = this.numeroDepositos + this.numeroRetiros;
        System.out.println("CUENTA DE AHORRO" + "\n" +
                "Saldo: " + this.saldo + "\n" +
                "Consignaciones: " + numeroDepositos + "\n" +
                "Retiros: " + numeroRetiros + "\n" +
                "Transacciones realizadas: " + numeroTransacciones);
    }

}