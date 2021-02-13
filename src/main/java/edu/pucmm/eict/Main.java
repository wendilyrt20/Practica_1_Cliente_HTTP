package edu.pucmm.eict;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Cliente HTTP -------> 2016-1579");
        System.out.println("URL a consultar");

        Scanner teclado = new Scanner(System.in);
        String url = teclado.nextLine();

        Connection.Response doc = Jsoup.connect(url).execute();
        String x = doc.body().toString();
        Document document = Jsoup.parse(x);

        int lineas = CantLinea(doc);
        int parrafo = CantParrafos(document);
        int imagen = CantImagParr(document);
        int[] formularios = FormCategorizado(document);
        System.out.println("Cantidad de linea en el doc HTML: " + lineas );
        System.out.println("Cantidad de párrafo en el doc HTML: " + parrafo);
        System.out.println("Cantidad de imáganes contenidas en párrafos: " + imagen);
        System.out.println("Cantidad de formularios son ->" + "GET: " +formularios[0] + "   POST: " + formularios[1] + "  Otros: " + formularios[2]);
        inputTipos(document);
        ResponseServer(url, document);
    }

    //a) Indicar la cantidad de lineas del recurso retornado.

    private static int CantLinea(Connection.Response doc) {
        return doc.body().split("\n").length;
    }

    //b) Indicar la cantidad de párrafos (p) que contiene el documento HTML.
    private static int CantParrafos(Document docu) {
        Elements parrafo = docu.getElementsByTag("p");
        return  parrafo.size();
    }


/* c) Indicar la cantidad de imágenes (img) dentro de los párrafos que
        contiene el archivo HTML.  */ //CAMBIO

    private static int CantImagParr(Document docu) {
        int valor =0;
        Elements parrafo = docu.getElementsByTag("p");
        for (Element element: parrafo) {
             Elements img = element.getElementsByTag("img");
             if(!img.isEmpty()){
                valor += img.size();
            }
        }
        return valor;
    }


/* d) indicar la cantidad de formularios (form) que contiene el HTML por
    categorizando por el método implementado POST o GET
 */

     private static int[] FormCategorizado(Document docu){
         int[] lista = {0,0,0};
         Elements formulario = docu.getElementsByTag("form");

         for (Element element: formulario) {

             if (!element.getElementsByAttributeValueContaining("method", "GET").isEmpty()){
                 lista[0]++;
             }
             if (!element.getElementsByAttributeValueContaining("method", "POST").isEmpty()){
                 lista[1]++;
             }
             else
                 lista[2]++;

         }
            return lista;
    }

    /*
    e) Para cada formulario mostrar los campos del tipo input y su
    respectivo tipo que contiene en el documento HTML.  */

    private static void inputTipos(Document docu){
        String[] result = {"",""};
        Elements formulario = docu.getElementsByTag("form");
        for (Element form: formulario) {
            Elements input = form.getElementsByTag("input") ;
            for (Element dato: input) {
             System.out.println("Formularios:" + form.siblingIndex() + " " + dato.attr("type"));
            }
        }
    }

    /* f) Para cada formulario “parseado”, identificar que el método de envío
    del formulario sea POST y enviar una petición al servidor con el
    parámetro llamado asignatura y valor practica1 y un header llamado
    matricula con el valor correspondiente a matrícula asignada. Debe
    mostrar la respuesta por la salida estándar. */

    private static void ResponseServer(String url, Document docu) throws IOException {
        Elements formulario = docu.getElementsByTag("form");
       // Connection.Request req=null;
       // String body_req = null;

        for (Element element: formulario) {
            if (!element.getElementsByAttributeValueContaining("method", "POST").isEmpty()){
                Connection.Response res = Jsoup.connect(url).data("parametro", "asignatura")
                                         .header("matricula", "20161579").
                                        method(Connection.Method.valueOf("POST")).execute();

                System.out.println("Estado de la solicitud: " + res.statusMessage() + " Estado del código: " + res.statusCode() );
                System.out.println("<------------CUERPO-------->: " );
                System.out.println(res.body());


            }


        }

    }


}

