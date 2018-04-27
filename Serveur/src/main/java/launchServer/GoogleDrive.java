package launchServer;


import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;


@Path("/Google")
public class GoogleDrive  {


    private static String access_token;
    private static String clientID = "1052251515610-a6n80d93kvrffileji09br2ksbnvdoj3.apps.googleusercontent.com";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String afficher(){
        return "<a href=http://localhost:8080/ServeurDrive/Google/Oauth"+">"+"click ici pour s'authentifier"+"</a>"+"<br>"+ "<a href=http://localhost:8080/ServeurDrive/Google/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>" + "<br>" +
                "<a href=http://localhost:8080/ServeurDrive/Google/Delete" + ">" + "supprimer un fichier" + "</a>" ;


    }

    @Path("/Oauth")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getAuthentification() throws URISyntaxException, IOException{


        String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fdrive&redirect_uri=http://localhost:8080/ServeurDrive/Google/Response&response_type=code&client_id="+clientID;

        java.net.URI location = new java.net.URI(url);
        return Response.temporaryRedirect(location).build();
    }


    @Path("/Response")
    @GET //A changer en POST
    public Response getResponse(@QueryParam("code") String codeURL) throws URISyntaxException, IOException {

        String code = codeURL;

        String clientSecret = "mPeJJscfiXdNGN9jma3croXB";

        String url = "https://www.googleapis.com/oauth2/v4/token";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Content-Type","application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code="+code + "&client_id="+clientID+ "&client_secret="+clientSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/Google/Response" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        System.out.println(response.toString());

        JSONObject myResponse = new JSONObject(response.toString());
        this.access_token = myResponse.getString("access_token");



    // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
    java.net.URI location = new java.net.URI("http://localhost:8080/ServeurDrive/Google");
    return Response.temporaryRedirect(location).build();

}

    @Path("/Files")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getFiles() throws IOException {


    String url = "https://www.googleapis.com/drive/v2/files";


    //les propiétés
    HashMap<String,String> properties = new HashMap<>();
    properties.put("Host", "www.googleapis.com");
    properties.put("Authorization", "Bearer " + this.access_token);

    // on execute la requête
    String response = HttpRequest.Request.setRequest(url,"GET","", properties);

    //print result

    return "<p> "+ response + "</p>";



}

    @Path("/Delete")
    @GET //A changer en DELETE
    @Produces(MediaType.TEXT_HTML)
    public String deleteFile() throws IOException {


        String fileID = "1L6s_6soghMDimTcEJ-fy6ixyfrHCnB0c15Q8B-abZZE";
        String url = "https://www.googleapis.com/drive/v2/files/" + fileID;

        System.out.println("coucuo");

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_token);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "DELETE", "", properties);

        return "<p>" + response + "</p>";
    }




}
