class Name {
	private String name = "JAVA";

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class Test {
	public static void main(String[] args) {
		Name name1 = new Name();
		Name name2 = new Name();
		System.out.println(name1.getName() == name2.getName());
		name1.setName("JAVA5");
		System.out.println(name1.getName() == name2.getName());

	}
}