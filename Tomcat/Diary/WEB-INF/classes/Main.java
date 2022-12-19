
import com.diary.server.database.DataBase;
import com.diary.encryption.EncryptDecrypt;
import com.diary.encryption.Hash;
import com.diary.filters.FilterController;
import com.diary.filters.FileAccess;
import com.diary.filters.RegisterFilter;
import com.diary.models.User;
import com.diary.models.Text;
  import com.diary.server.authentication.Controllers.Controller;
 import com.diary.server.authentication.Controllers.SessionController;
import com.diary.clients.authentication.Login;
import com.diary.clients.authentication.Register;
import com.diary.clients.operation.Search;
import com.diary.clients.authentication.UserDetails;
import com.diary.server.operation.SearchController;
// import com.diary.servlets.operation.Edit;
 import com.diary.clients.authentication.SessionRequest;
//  import com.diary.clients.operation.Search;
//   import com.diary.server.operation.Controller;
// import com.diary.servlets.operation.Get;
// import com.diary.servlets.operation.Edit;

import com.diary.models.Text;
import com.diary.server.operation.ClearSession;
import com.diary.clients.operation.Option;
// import com.diary.servlets.operation.LastTwo;
// import com.diary.servlets.operation.SearchDates;
// import com.diary.servlets.operation.DeleteNote;
// import com.diary.servlets.authentication.UpdateBio;
// import com.diary.servlets.operation.StarNote;
// import com.diary.servlets.operation.Controllers.OptionController;
//  import com.diary.servlets.operation.Controllers.SearchController;
// import com.diary.servlets.operation.GetStaredNote;
// import com.diary.servlets.authentication.Logout;
// import com.diary.servlets.authentication.GetBio;
import com.diary.clients.operation.Upload;
import com.diary.server.operation.UploadController;
public class Main {
   public static void main(String[] args) throws Exception {
      Hash hash = new Hash();
      String key = hash.toSHA1("Prasanth1", "MD5");
       System.out.println(key);
      // String skey = EncryptDecrypt.generateKey();
      // System.out.println("skey\n"+skey);
      String decrypt = EncryptDecrypt.decrypt("yKjS2BiqOXPfwd6+4jB1ePb4GdZAyqjjBBAB+Ew+P20=", key);
      // String decrypt = EncryptDecrypt.decrypt(enc, key);
      System.out.println(decrypt);
   }
}
