import java.util.ArrayList;
import java.util.List;

public class BreakStatement {
    static void breakStatement(int iter, List<Integer> list) {
        List<BreakStatementFrame> stack = new ArrayList<>();
        stack.add(new BreakStatementFrame(iter, list));
        while (true) {
            BreakStatementFrame frame = stack.get(stack.size() - 1);
            switchLabel:
            switch (frame.block) {
                case 0: {
                    if (frame.iter == 0) {
                        if (stack.size() == 1)
                            return;
                        stack.remove(stack.size() - 1);
                        break switchLabel;
                    }
                    frame.count = frame.iter;
                    while (true) {
                        frame.list.add(frame.iter);
                        frame.count--;
                        if (frame.count == 0) {
                            break;
                        }
                    }
                    stack.add(new BreakStatementFrame(frame.iter - 1, frame.list));
                    frame.block = 1;
                    break switchLabel;
                }
                case 1: {
                    if (stack.size() == 1)
                        return;
                    stack.remove(stack.size() - 1);
                    break switchLabel;
                }
            }
        }
    }

    private static class BreakStatementFrame {
        private int iter;
        private List<Integer> list;
        private int count;
        private int block;

        private BreakStatementFrame(int iter, List<Integer> list) {
            this.iter = iter;
            this.list = list;
        }
    }

    public static void main(String[] args) {
        final ArrayList<Integer> list = new ArrayList<>();
        BreakStatement.breakStatement(4, list);
        System.out.println(list);
    }
}
