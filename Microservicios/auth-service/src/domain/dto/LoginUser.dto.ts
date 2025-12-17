import { regularExps } from "../../config/regular-exp.js";

export class LoginUserDto {
  private constructor(
    public email: string,
    public password: string
  ) {}

  static create( object: {[key:string]:any} ):[string | undefined, LoginUserDto | undefined] {
    const { email, password } = object;

    if(!email) return ['Missing Email', undefined];

    if(!regularExps.email.test(email)) return ['Email is not valid', undefined];
    if(!password) return ['Missing Password', undefined];
    if(password.length < 6) return ['Password must be at least 6 characters', undefined];

    return [undefined, new LoginUserDto(email, password)];
  }
}