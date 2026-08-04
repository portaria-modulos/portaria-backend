package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

public class EntregaToken implements DevolucaoInteface{
    public String msg;
    public String type;

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }
    @Override
    public void setType(String type) {
        this.type = type;
    }
}
