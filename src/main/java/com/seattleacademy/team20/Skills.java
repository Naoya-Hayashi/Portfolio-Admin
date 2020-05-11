package com.seattleacademy.team20;

public class Skills {

  private String id;
  private String category;
  private String name;
  private int score;

  public Skills(String id, String category, String name, int score) {
    // TODO 自動生成されたコンストラクター・スタブ
    this.id = id;
    this.category = category;
    this.name = name;
    this.score = score;
  }

  public String getId() {

    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSkillCategory() {
    return category;
  }

  public void setSkillCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}

}