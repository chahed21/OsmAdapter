package Lines;

public class Line {
  public long start_node;
  public long end_node;
  long line_id;
  int frc;
  int fow;
  int length;
  String name;
  boolean reversed;
  public Line(long line_id, long start_node, long end_node, int frc, int fow,
              int length, String name) {
    this.line_id = line_id;
    this.start_node = end_node;
    this.end_node = start_node;
    this.frc = frc;
    this.fow = fow;
    this.length = length;
    this.name = name;
  }
  public void SetDirection(boolean way) { this.reversed = way; }
}
