/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.idomsoft.szemelyszerviz.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ruzsinak
 */
@Data
public class SzemelyDTO implements Serializable{

    private static final long serialVersionUID = 4L;

    @ApiModelProperty("Viselt név (Legalább két névelemnek kell lennie, a kezdő vagy befejező Dr.-on kívül magyar ABC plussz Ä, pont, perjel, aposztróf, kötőjel és szóköz Max 80)")
    private String visNev;

    @ApiModelProperty("Születési név (Legalább két névelemnek kell lennie, a kezdő vagy befejező Dr.-on kívül magyar ABC plussz Ä, pont, perjel, aposztróf, kötőjel és szóköz Max 80)")
    private String szulNev;

    @ApiModelProperty("Anyja neve  (Legalább két névelemnek kell lennie, a kezdő vagy befejező Dr.-on kívül magyar ABC plussz Ä, pont, perjel, aposztróf, kötőjel és szóköz Max 80)")
    private String aNev;

    @ApiModelProperty("Születési idő (Dátum, min 18, max 120 éves)")
    private Date szulDat;

    @ApiModelProperty("Neme, F=férfi, N=nő")
    private String neme;

    @ApiModelProperty("Állampolgárság, kódszótár tartalmazza")
    private String allampKod;

    @ApiModelProperty("Nem kell megadni, webszerviz tölti ki az allampKod mező alapján")
    private String allampDekod;

    @ApiModelProperty("Az illető okmányainak listája. Egy személyhez tartozhat több ugyanolyan típusú okmány, de egy típusból csak egy lehet érvényes.")
    private ArrayList<OkmanyDTO> okmLista;
    
}
