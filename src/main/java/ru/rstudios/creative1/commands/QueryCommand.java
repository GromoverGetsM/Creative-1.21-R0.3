package ru.rstudios.creative1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QueryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("creative.query")) {
            sender.sendMessage("§cУ вас нет прав для выполнения этой команды.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cИспользуйте: /query <SQLQuery>");
            return true;
        }

        String sqlQuery = String.join(" ", args).trim();

        if (sqlQuery.toLowerCase().startsWith("select")) {
            handleSelectQuery(sender, sqlQuery);
        } else {
            handleUpdateQuery(sender, sqlQuery);
        }

        return true;
    }

    private void handleSelectQuery(CommandSender sender, String sqlQuery) {
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseUtil.executeQueryNoAutoClosed(sqlQuery);

            if (resultSet == null || !resultSet.next()) {
                sender.sendMessage("§cЗапрос не вернул данных.");
                return;
            }

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder resultMessage = new StringBuilder("§aРезультаты запроса:");
            do {
                resultMessage.append("\n§e[");
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = resultSet.getString(i);
                    resultMessage.append(metaData.getColumnName(i)).append(": ").append(columnValue);
                    if (i < columnCount) {
                        resultMessage.append(", ");
                    }
                }
                resultMessage.append("]");
            } while (resultSet.next());

            sender.sendMessage(resultMessage.toString());
        } catch (SQLException e) {
            sender.sendMessage("§cОшибка выполнения запроса: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.getStatement().close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUpdateQuery(CommandSender sender, String sqlQuery) {
        try {
            DatabaseUtil.executeUpdate(sqlQuery);
            sender.sendMessage("§aЗапрос успешно выполнен.");
        } catch (Exception e) {
            sender.sendMessage("§cОшибка выполнения запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
