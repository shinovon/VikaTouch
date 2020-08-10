package vikatouch.base.attachments;

// Нужен для объединения разных объектов ВК, умеющих в социалку. Например, в ImagePreview. Пригодится короче.
public interface ISocialable {
	public void Save(); // сохранить себе
	public void Like(boolean val); // 1 - поставить лайк, 0 - убрать.
	public void Send(); // отправить в лс
	public void Repost(); // репост. Всё кроме постов - при вызове ничего не делать.
	public void OpenComments(); // открыть экран комментариев
	//public void SendComment(Comment comment); // отправить коммент
}
