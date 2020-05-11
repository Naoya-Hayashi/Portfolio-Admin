package com.seattleacademy.team20;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SkillController<Skill> {

  private static final Logger logger = LoggerFactory.getLogger(SkillController.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Simply selects the home view to render by returning its name.
   *
   * @throuws IOException
   */
  @RequestMapping(value = "/upload", method = RequestMethod.GET)
  public String uploadSkill(Locale locale, Model model) {
    logger.info("Welcome home! The client locale is {}.", locale);

    try {
      initialize();
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
    ;

    List<Skills> skills = selectskills();
    uploadSkill(skills);

    return "skill-upload";
  }

  // ここからタスク10
  //
  // Listの宣言
  public List<Skills> selectskills() {
    // sequel proで作ったテーブルからデータを取得する文字列をsqlという変数に入れている
    final String sql = "select * from skills";

    return jdbcTemplate.query(sql, new RowMapper<Skills>() {
      public Skills mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Skills(rs.getString("id"), rs.getString("category"), rs.getString("name"), rs.getInt("score"));

      }
    });
  }

  private FirebaseApp app;

  //
  // SDKの初期化
  public void initialize() throws IOException {
    FileInputStream refreshToken = new FileInputStream(
        "/Users/nao8/Downloads/develop-protfolio2020121-firebase-adminsdk-qcr8z-2b0605b198.json");
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(refreshToken))
        .setDatabaseUrl("https://develop-protfolio2020121.firebaseio.com/").build();
    app = FirebaseApp.initializeApp(options, "other");
  }

  //
  public void uploadSkill(List<Skills> skills) {
    // データの保存
    final FirebaseDatabase database = FirebaseDatabase.getInstance(app);
    DatabaseReference ref = database.getReference("skills");
    //
    // Map型のリストを作る。MapはStringで聞かれたものに対し、Object型で返すようにしている
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map;
    Map<String, List<Skills>> skillMap = skills.stream().collect(Collectors.groupingBy(Skills::getSkillCategory));
    for (Map.Entry<String, List<Skills>> entry : skillMap.entrySet()) {
      map = new HashMap<>();
      map.put("category", entry.getKey());
      map.put("skill", entry.getValue());
      dataList.add(map);
    }
    // 非互換オペランド型 String と intとは？↑
    //
    ref.setValue(dataList, new DatabaseReference.CompletionListener() {
      @Override
      public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError != null) {
          System.out.println("Data could be saved" + databaseError.getMessage());
        } else {
          System.out.println("Data save successfully.");
        }
      }
    });
  }
}
