import java.sql.Connection;
import java.sql.PreparedStatement;

import com.diary.connection.DataBase;
import com.diary.encryption.Hash;
import com.diary.filters.FilterController;
import com.diary.filters.LoginFilter;
import com.diary.filters.RegisterFilter;
import com.diary.models.User;
import com.diary.servlets.authentication.Controllers.Controller;
import com.diary.servlets.authentication.Controllers.SessionController;
import com.diary.servlets.authentication.Login;
import com.diary.servlets.authentication.Register;
import com.diary.servlets.operation.Edit;
import com.diary.servlets.authentication.SessionRequest;
import com.diary.servlets.operation.Get;
import com.diary.servlets.operation.Edit;

import com.diary.models.Text;
import com.diary.servlets.operation.LastTwo;
import com.diary.servlets.operation.SearchDates;
import com.diary.servlets.operation.DeleteNote;
import com.diary.servlets.authentication.UpdateBio;
import com.diary.servlets.operation.StarNote;
import com.diary.servlets.operation.Controllers.OptionController;
import com.diary.servlets.operation.Controllers.SearchController;
import com.diary.servlets.operation.GetStaredNote;
import com.diary.servlets.authentication.Logout;
import com.diary.servlets.authentication.GetBio;
public class Main {
   public static void main(String[] args) {
      Hash hash = new Hash();
      System.out.println(hash.toSHA1("Prasanth1"));
   }
}
