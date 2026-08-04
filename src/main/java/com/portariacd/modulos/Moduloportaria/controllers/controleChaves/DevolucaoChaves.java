package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

public class DevolucaoChaves implements  DevolucaoInteface{
    String type;
    String msg;
    @Override
    public void setType(String type) {
      this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
